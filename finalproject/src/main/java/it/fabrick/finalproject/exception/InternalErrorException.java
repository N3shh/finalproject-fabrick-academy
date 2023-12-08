package it.fabrick.finalproject.exception;


import it.fabrick.finalproject.enumeration.ErrorCode;
import lombok.Getter;

@Getter
public class InternalErrorException extends RuntimeException {

    private final ErrorCode errorCode;

    public InternalErrorException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
