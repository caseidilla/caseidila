package org.codet.caseidilla.exception;

public class CaseidillaException extends RuntimeException {

    public CaseidillaException(String message) {
        super(message);
    }

    public CaseidillaException(String message, Throwable cause) {
        super(message, cause);
    }
}
