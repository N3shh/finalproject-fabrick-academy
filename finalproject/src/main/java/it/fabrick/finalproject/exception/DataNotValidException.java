package it.fabrick.finalproject.exception;

import it.fabrick.finalproject.enumeration.ErrorCode;
import lombok.Getter;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class DataNotValidException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Set<ConstraintViolation<Object>> constraintViolations;

    public DataNotValidException(ErrorCode errorCode, Set<ConstraintViolation<Object>> constraintViolations) {
        this.errorCode = errorCode;
        this.constraintViolations = constraintViolations;
    }

    @Override
    public String getMessage() {
        if (constraintViolations.isEmpty()) {
            return super.getMessage();
        } else {
            return constraintViolations.stream()
                    .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                    .collect(Collectors.joining(", "));
        }
    }
}
