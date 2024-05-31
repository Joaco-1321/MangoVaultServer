package io.joaco.mangovaultserver.facade;

import io.joaco.mangovaultserver.domain.dto.FriendRequestData;
import io.joaco.mangovaultserver.domain.model.FriendRequest;
import io.joaco.mangovaultserver.domain.model.FriendRequest.FriendRequestStatus;
import io.joaco.mangovaultserver.domain.model.User;
import io.joaco.mangovaultserver.service.FriendRequestService;
import io.joaco.mangovaultserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

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

        messagingTemplate.convertAndSendToUser(recipient.getUsername(), "/queue/notification", friendRequestdata);
    }

    public void cancelFriendRequest(FriendRequestData friendRequestData) {
        operateFriendRequest(friendRequestData, FriendRequestStatus.CANCELED);
    }

    public void acceptFriendRequest(FriendRequestData friendRequestData) {
        FriendRequest request = operateFriendRequest(friendRequestData, FriendRequestStatus.ACCEPTED);

        User requester = request.getRequester();

        requester.getFriends()
                 .add(request.getRecipient());

        userService.save(requester);
    }

    public void rejectFriendRequest(FriendRequestData friendRequestData) {
        operateFriendRequest(friendRequestData, FriendRequestStatus.REJECTED);
    }

    public void removeFriend(String username1, String username2) {
        Optional<Pair<User, User>> friends = isFriend(username1, username2);

        friends.ifPresent((pair) -> {
            pair.getFirst()
                .getFriends()
                .remove(pair.getSecond());

            userService.save(pair.getFirst());
        });
    }

    private Optional<Pair<User, User>> isFriend(String username1, String username2) {
        User requester = userService.findByUsername(username1);
        User recipient = userService.findByUsername(username2);
        Optional<Pair<User, User>> friends = Optional.empty();

        if (requester.getFriends()
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
                                               "/queue/notification",
                                               friendRequestData);

        return request;
    }
}
