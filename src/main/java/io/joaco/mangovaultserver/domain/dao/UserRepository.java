package io.joaco.mangovaultserver.domain.dao;

import io.joaco.mangovaultserver.domain.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = """
                   SELECT * FROM users u WHERE u.id IN (
                   SELECT user_id1 FROM friends WHERE user_id2 = (SELECT id FROM users WHERE username = :username)
                   UNION
                   SELECT user_id2 FROM friends WHERE user_id1 = (SELECT id FROM users WHERE username = :username))""",
           nativeQuery = true)
    Set<User> findFriendsByUsername(@Param("username") String username);

}
