package io.joaco.mangovaultserver.exception;

public class AlreadyExistsException extends GenericKeyException {

    public AlreadyExistsException(String key, String message) {
        super(key, message);
    }
}
