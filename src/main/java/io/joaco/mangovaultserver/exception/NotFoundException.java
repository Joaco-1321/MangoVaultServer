package io.joaco.mangovaultserver.exception;

public class NotFoundException extends GenericKeyException {

    public NotFoundException(String key, String message) {
        super(key, message);
    }
}
