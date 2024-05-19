package io.joaco.mangovaultserver.service;

import io.joaco.mangovaultserver.dto.UserData;
import io.joaco.mangovaultserver.exception.UsernameAlreadyExists;
import io.joaco.mangovaultserver.persistence.dao.UserRepository;
import io.joaco.mangovaultserver.persistence.model.Role;
import io.joaco.mangovaultserver.persistence.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void register(UserData user) throws UsernameAlreadyExists {
        if (userExists(user.getUsername())) {
            throw new UsernameAlreadyExists();
        }

        userRepository.save(User.builder()
                                .username(user.getUsername())
                                .password(passwordEncoder.encode(user.getPassword()))
                                .enabled(true)
                                .role(Role.builder()
                                          .role("USER")
                                          .build())
                                .build());
    }

    private boolean userExists(String username) {
        return userRepository.findByUsername(username)
                             .isPresent();
    }
}
