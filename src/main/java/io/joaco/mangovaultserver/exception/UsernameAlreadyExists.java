package io.joaco.mangovaultserver.exception;

public class UsernameAlreadyExists extends RuntimeException {

    public UsernameAlreadyExists() {
    }

    public UsernameAlreadyExists(String message) {
        super(message);
    }

    public UsernameAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyExists(Throwable cause) {
        super(cause);
    }
}
