package io.joaco.mangovaultserver.domain.dao;

import io.joaco.mangovaultserver.domain.model.FriendRequest;
import io.joaco.mangovaultserver.domain.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {

    Optional<FriendRequest> findByRequesterAndRecipient(User requester, User recipient);

    @Query(value = """
                   SELECT f FROM FriendRequest f
                   WHERE f.recipient = (SELECT u FROM User u WHERE u.username = :username)
                   OR f.requester = (SELECT u FROM User u WHERE u.username = :username)""")
    Set<FriendRequest> findByRequesterOrRecipient(@Param("username") String username);
}
