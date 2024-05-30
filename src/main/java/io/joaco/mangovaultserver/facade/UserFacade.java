package io.joaco.mangovaultserver.facade;

import io.joaco.mangovaultserver.domain.dto.UserAuthData;
import io.joaco.mangovaultserver.domain.dto.UserDetailsData;
import io.joaco.mangovaultserver.domain.model.User;
import io.joaco.mangovaultserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsData registerUser(UserAuthData userAuthData) {
        userService.exists(userAuthData.getUsername());

        User newUser = userService.save(User.builder()
                                            .username(userAuthData.getUsername())
                                            .passwordHash(passwordEncoder.encode(userAuthData.getPassword()))
                                            .build());

        return UserDetailsData.builder()
                              .username(newUser.getUsername())
                              .build();
    }
}
