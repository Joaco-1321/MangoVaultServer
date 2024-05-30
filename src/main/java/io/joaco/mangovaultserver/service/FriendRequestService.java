package io.joaco.mangovaultserver.service;

import io.joaco.mangovaultserver.domain.dao.FriendRequestRepository;
import io.joaco.mangovaultserver.domain.model.FriendRequest;
import io.joaco.mangovaultserver.domain.model.User;
import io.joaco.mangovaultserver.exception.AlreadyExistsException;
import io.joaco.mangovaultserver.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    public FriendRequest findByRequesterAndRecipient(User requester, User recipient) {
        return friendRequestRepository.findByRequesterAndRecipient(requester, recipient)
                                      .orElseThrow(() -> new NotFoundException("friend request",
                                                                               "friend request not found"));
    }

    public void exists(User requester, User recipient) {
        friendRequestRepository.findByRequesterAndRecipient(requester, recipient)
                               .ifPresent((friendRequest) -> {
                                   throw new AlreadyExistsException("friend request", "friend request already exists");
                               });
    }

    public FriendRequest save(FriendRequest friendRequest) {
        return friendRequestRepository.save(friendRequest);
    }
}