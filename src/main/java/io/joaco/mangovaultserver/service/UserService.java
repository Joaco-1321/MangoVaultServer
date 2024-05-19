package io.joaco.mangovaultserver.service;

import io.joaco.mangovaultserver.dto.UserData;
import io.joaco.mangovaultserver.exception.UsernameAlreadyExists;
import io.joaco.mangovaultserver.persistence.dao.RoleRepository;
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

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public void register(UserData user) throws UsernameAlreadyExists {
        if (userExists(user.getUsername())) {
            throw new UsernameAlreadyExists("username already exists");
        }

        roleRepository.save(Role.builder()
                                .role("USER")
                                .user(userRepository.save(User.builder()
                                                              .username(user.getUsername())
                                                              .password(passwordEncoder.encode(user.getPassword()))
                                                              .enabled(true)
                                                              .build()))
                                .build());
        ;
    }

    private boolean userExists(String username) {
        return userRepository.findByUsername(username)
                             .isPresent();
    }
}
