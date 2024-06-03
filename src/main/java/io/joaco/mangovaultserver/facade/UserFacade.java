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

    private final FriendFacade friendFacade;

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

    public String getKey(String username, String friend) {
        friendFacade.isFriendOrThrow(username, friend);

        return userService.findByUsername(friend).getPublicKeyEncoded();
    }

    public void publishKey(String username, String key) {
        User user = userService.findByUsername(username);

        user.setPublicKeyEncoded(key);

        userService.save(user);
    }
}
