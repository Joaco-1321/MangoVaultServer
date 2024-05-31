package io.joaco.mangovaultserver.service;

import io.joaco.mangovaultserver.domain.dao.FriendRequestRepository;
import io.joaco.mangovaultserver.domain.model.FriendRequest;
import io.joaco.mangovaultserver.domain.model.FriendRequest.FriendRequestStatus;
import io.joaco.mangovaultserver.domain.model.User;
import io.joaco.mangovaultserver.exception.AlreadyExistsException;
import io.joaco.mangovaultserver.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    public FriendRequest getByRequesterAndRecipientOrElseNew(User requester, User recipient) {
        return friendRequestRepository.findByRequesterAndRecipient(requester, recipient)
                                      .orElseGet(() -> FriendRequest.builder()
                                                                    .requester(requester)
                                                                    .recipient(recipient)
                                                                    .build());
    }

    public FriendRequest findByRequesterAndRecipient(User requester, User recipient) {
        return friendRequestRepository.findByRequesterAndRecipient(requester, recipient)
                                      .orElseThrow(() -> new NotFoundException("friend request",
                                                                               "friend request not found"));
    }

    public Set<FriendRequest> findByRequesterOrRecipient(String username) {
        return friendRequestRepository.findByRequesterOrRecipient(username);
    }

    public void throwIfUnavailable(User requester, User recipient) {
        friendRequestRepository.findByRequesterAndRecipient(requester, recipient)
                               .filter(friendRequest -> friendRequest.getStatus() != FriendRequestStatus.CANCELED && friendRequest.getStatus() != FriendRequestStatus.REJECTED)
                               .ifPresent((friendRequest) -> {
                                   throw new AlreadyExistsException("friend request", "friend request already exists");
                               });
    }

    public FriendRequest save(FriendRequest friendRequest) {
        return friendRequestRepository.save(friendRequest);
    }
}
