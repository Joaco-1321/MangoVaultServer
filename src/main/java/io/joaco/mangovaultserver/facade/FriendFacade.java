package io.joaco.mangovaultserver.facade;

import io.joaco.mangovaultserver.domain.dto.FriendRequestData;
import io.joaco.mangovaultserver.domain.model.FriendRequest;
import io.joaco.mangovaultserver.domain.model.FriendRequest.FriendRequestStatus;
import io.joaco.mangovaultserver.domain.model.User;
import io.joaco.mangovaultserver.exception.GenericKeyException;
import io.joaco.mangovaultserver.service.FriendRequestService;
import io.joaco.mangovaultserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FriendFacade {

    private final UserService userService;

    private final FriendRequestService friendRequestService;

    private final SimpMessagingTemplate messagingTemplate;

    public Set<String> getFriendsByUsername(String username) {
        return userService.getFriends(username)
                          .stream()
                          .map(User::getUsername)
                          .collect(Collectors.toSet());
    }

    public Set<FriendRequestData> getFriendRequestsByUsername(String username) {
        return friendRequestService.findByRequesterOrRecipient(username)
                                   .stream()
                                   .map(request -> FriendRequestData.builder()
                                                                    .requester(request.getRequester()
                                                                                      .getUsername())
                                                                    .recipient(request.getRecipient()
                                                                                      .getUsername())
                                                                    .status(request.getStatus())
                                                                    .build())
                                   .collect(Collectors.toSet());
    }

    public void sendFriendRequest(FriendRequestData friendRequestdata) {
        User requester = userService.findByUsername(friendRequestdata.getRequester());
        User recipient = userService.findByUsername(friendRequestdata.getRecipient());

        friendRequestService.throwIfUnavailable(requester, recipient);

        FriendRequest request = friendRequestService.getByRequesterAndRecipientOrElseNew(requester, recipient);

        if (request.getStatus() == FriendRequestStatus.CANCELED || request.getStatus() == FriendRequestStatus.REJECTED) {
            request.setStatus(FriendRequestStatus.PENDING);
            friendRequestdata.setStatus(FriendRequestStatus.PENDING);
        }

        friendRequestService.save(request);

        messagingTemplate.convertAndSendToUser(recipient.getUsername(),
                                               "/queue/notification/request",
                                               friendRequestdata);
    }

    public void cancelFriendRequest(FriendRequestData friendRequestData) {
        operateFriendRequest(friendRequestData, FriendRequestStatus.CANCELED);
    }

    public void acceptFriendRequest(FriendRequestData friendRequestData) {
        FriendRequest request = operateFriendRequest(friendRequestData, FriendRequestStatus.ACCEPTED);
        User requester = request.getRequester();
        User recipient = request.getRecipient();

        if (requester.getId() < recipient.getId()) {
            requester.getFriends()
                     .add(recipient);
        } else {
            recipient.getFriends()
                     .add(requester);
        }

        userService.save(requester);
        userService.save(recipient);
    }

    public void rejectFriendRequest(FriendRequestData friendRequestData) {
        operateFriendRequest(friendRequestData, FriendRequestStatus.REJECTED);
    }

    @Transactional
    public void removeFriend(String username1, String username2) {
        Optional<Pair<User, User>> friends = isFriend(username1, username2);

        friends.ifPresent((pair) -> {
            User first = pair.getFirst();
            User second = pair.getSecond();

            first.getFriends().remove(second);
            second.getFriends().remove(first);

            friendRequestService.findByRequesterAndRecipientOptional(first, second)
                                .ifPresent(request -> {
                                    request.setStatus(FriendRequestStatus.CANCELED);
                                    friendRequestService.save(request);
                                });

            friendRequestService.findByRequesterAndRecipientOptional(second, first)
                                .ifPresent(request -> {
                                    request.setStatus(FriendRequestStatus.CANCELED);
                                    friendRequestService.save(request);
                                });

            userService.save(first);
            userService.save(second);

            messagingTemplate.convertAndSendToUser(username2, "/queue/notification/remove", username1);
        });
    }

    @Transactional
    public void isFriendOrThrow(String username1, String username2) {
        isFriend(username1, username2).orElseThrow(() -> new GenericKeyException("friend", "users are not friends"));
    }

    private Optional<Pair<User, User>> isFriend(String username1, String username2) {
        User requester = userService.findByUsername(username1);
        User recipient = userService.findByUsername(username2);
        Optional<Pair<User, User>> friends = Optional.empty();

        if (userService.getFriends(requester.getUsername())
                       .contains(recipient)) {
            friends = Optional.of(Pair.of(requester, recipient));
        }

        return friends;
    }

    private FriendRequest operateFriendRequest(FriendRequestData friendRequestData, FriendRequestStatus status) {
        User requester = userService.findByUsername(friendRequestData.getRequester());
        User recipient = userService.findByUsername(friendRequestData.getRecipient());
        FriendRequest request = friendRequestService.findByRequesterAndRecipient(requester, recipient);

        request.setStatus(status);
        friendRequestService.save(request);

        messagingTemplate.convertAndSendToUser(status == FriendRequestStatus.CANCELED
                                                       ? recipient.getUsername()
                                                       : requester.getUsername(),
                                               "/queue/notification/request",
                                               friendRequestData);

        return request;
    }
}
