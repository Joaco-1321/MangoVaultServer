package io.joaco.mangovaultserver.service;

import io.joaco.mangovaultserver.domain.dto.UserAuthData;
import io.joaco.mangovaultserver.domain.dto.UserDetailsData;
import io.joaco.mangovaultserver.exception.UsernameAlreadyExistsException;
import io.joaco.mangovaultserver.domain.dao.RoleRepository;
import io.joaco.mangovaultserver.domain.dao.UserRepository;
import io.joaco.mangovaultserver.domain.model.Role;
import io.joaco.mangovaultserver.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsData register(UserAuthData user) throws UsernameAlreadyExistsException {
        if (userExists(user.getUsername())) {
            throw new UsernameAlreadyExistsException("username already exists");
        }

        User newUser;

        roleRepository.save(Role.builder()
                                .role("USER")
                                .user((newUser = userRepository.save(User.builder()
                                                                         .username(user.getUsername())
                                                                         .passwordHash(passwordEncoder.encode(user.getPassword()))
                                                                         .enabled(true)
                                                                         .build())))
                                .build());

        return UserDetailsData.builder()
                              .username(newUser.getUsername())
                              .friends(newUser.getFriends()
                                              .stream()
                                              .map(User::getUsername)
                                              .collect(Collectors.toSet()))
                              .build();
    }

    private boolean userExists(String username) {
        return userRepository.findByUsername(username)
                             .isPresent();
    }
}
