package io.joaco.mangovaultserver.controller.exception;

import io.joaco.mangovaultserver.domain.dto.ErrorData;
import io.joaco.mangovaultserver.exception.AlreadyExistsException;
import io.joaco.mangovaultserver.exception.GenericKeyException;
import io.joaco.mangovaultserver.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorData errorData = new ErrorData();

        ex.getBindingResult()
          .getAllErrors()
          .forEach((error) -> errorData.getErrors()
                                       .put(((FieldError) error).getField(), error.getDefaultMessage()));

        return handleExceptionInternal(ex, errorData, headers, status, request);
    }

    @ExceptionHandler({ AlreadyExistsException.class, NotFoundException.class, GenericKeyException.class })
    public ResponseEntity<?> handleUsernameAlreadyExists(GenericKeyException ex) {
        return ResponseEntity.badRequest()
                             .body(ErrorData.builder()
                                            .error(ex.getKey(), ex.getMessage())
                                            .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleUsernameAlreadyExists(ConstraintViolationException ex) {
        return ResponseEntity.badRequest()
                             .body(ErrorData.builder()
                                            .error("misc", ex.getMessage())
                                            .build());
    }
}
