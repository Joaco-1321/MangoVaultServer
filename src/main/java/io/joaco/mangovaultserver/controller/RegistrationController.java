package io.joaco.mangovaultserver.controller;

import io.joaco.mangovaultserver.dto.UserData;
import io.joaco.mangovaultserver.exception.UsernameAlreadyExists;
import io.joaco.mangovaultserver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/user")
    ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserData user) {
        Map<String, String> response = new HashMap<>();

        userService.register(user);

        response.put("message", "user registered successfully");

        return ResponseEntity.ok(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
          .getAllErrors()
          .forEach((error) -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));

        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameAlreadyExists.class)
    public Map<String, String> handleUsernameAlreadyExistsException(UsernameAlreadyExists ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("error", ex.getMessage());

        return errors;
    }
}