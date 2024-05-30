package io.joaco.mangovaultserver.domain.dao;

import io.joaco.mangovaultserver.domain.model.FriendRequest;
import io.joaco.mangovaultserver.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {

    Optional<FriendRequest> findByRequesterAndRecipient(User requester, User recipient);
}
