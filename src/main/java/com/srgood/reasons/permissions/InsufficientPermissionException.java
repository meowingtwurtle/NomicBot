package com.srgood.reasons.permissions;

public class InsufficientPermissionException extends RuntimeException {
    public InsufficientPermissionException() {
        super();
    }

    public InsufficientPermissionException(String message) {
        super(message);
    }

    public InsufficientPermissionException(Throwable cause) {
        super(cause);
    }

    public InsufficientPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
