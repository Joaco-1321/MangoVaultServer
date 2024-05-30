package io.joaco.mangovaultserver.exception;

import lombok.Getter;

@Getter
public class GenericKeyException extends RuntimeException {

    private final String key;

    public GenericKeyException(String key, String message) {
        super(message);
        this.key = key;
    }
}
