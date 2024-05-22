package io.joaco.mangovaultserver.domain.dao;

import io.joaco.mangovaultserver.domain.model.FriendRequest;
import io.joaco.mangovaultserver.domain.model.FriendRequest.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findByRecipientAndStatus(String recipient, FriendRequestStatus status);

    List<FriendRequest> findByRequesterAndStatus(String requester, FriendRequestStatus status);
}
