package io.joaco.mangovaultserver.service;

import io.joaco.mangovaultserver.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthService {
    private static final List<User> users = new ArrayList<>(List.of(
            new User("koki", "kyle"),
            new User("lusia", "sapa"),
            new User("pepe", "grillo"),
            new User("lola", "lele")
    ));

    public static Optional<User> authenticate(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username)
                        && user.getPassword().equals(password))
                .findFirst();
    }
}
