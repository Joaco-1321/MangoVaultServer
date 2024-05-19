package io.joaco.mangovaultserver.persistence.dao;

import io.joaco.mangovaultserver.persistence.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
