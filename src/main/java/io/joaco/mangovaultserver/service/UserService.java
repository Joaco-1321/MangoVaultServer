package io.joaco.mangovaultserver.service;

import io.joaco.mangovaultserver.domain.dao.UserRepository;
import io.joaco.mangovaultserver.domain.model.User;
import io.joaco.mangovaultserver.exception.AlreadyExistsException;
import io.joaco.mangovaultserver.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                             .orElseThrow(() -> new NotFoundException("username", "user with username \"" + username + "\" does not exists"));
    }

    public void exists(String username) {
        userRepository.findByUsername(username)
                      .ifPresent((user) -> {
                          throw new AlreadyExistsException("username", "user with username \"" + username + "\" already exists");
                      });
    }

    public Set<User> getFriends(String username) {
        return userRepository.findFriendsByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
