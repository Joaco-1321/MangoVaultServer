package io.joaco.mangovaultserver.controller;

import io.joaco.mangovaultserver.domain.dto.UserAuthData;
import io.joaco.mangovaultserver.facade.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    private final UserFacade userFacade;

    @PostMapping("/user")
    ResponseEntity<?> register(@Valid @RequestBody UserAuthData user) {
        return ResponseEntity.ok(userFacade.registerUser(user));
    }
}
