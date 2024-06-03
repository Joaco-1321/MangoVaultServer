package io.joaco.mangovaultserver.controller;

import io.joaco.mangovaultserver.domain.dto.UserAuthData;
import io.joaco.mangovaultserver.facade.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/user")
    ResponseEntity<?> register(@Valid @RequestBody UserAuthData user) {
        return ResponseEntity.ok(userFacade.registerUser(user));
    }


    @PostMapping("/user/key")
    ResponseEntity<?> publishKey(Principal principal, @RequestBody String key) {
        userFacade.publishKey(principal.getName(), key);

        return ResponseEntity.ok()
                             .build();
    }

    @GetMapping("/user/key/{username}")
    ResponseEntity<?> getKey(Principal principal, @PathVariable String username) {
        return ResponseEntity.ok(userFacade.getKey(principal.getName(), username));
    }
}
