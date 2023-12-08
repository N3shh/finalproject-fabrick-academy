package it.fabrick.finalproject.handler;

import it.fabrick.finalproject.dto.ErrorResponseDto;
import it.fabrick.finalproject.enumeration.ErrorCode;
import it.fabrick.finalproject.exception.BadRequestException;
import it.fabrick.finalproject.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException badRequestException) {
        log.error("Bad request exception", badRequestException);
        return ResponseEntity.badRequest().body(ErrorResponseDto.builder()
                .errorCode(badRequestException.getErrorCode())
                .errorMessage(badRequestException.getMessage())
                .build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException validationException) {
        log.error("Validation exception", validationException);

        Set<ConstraintViolation<Object>> constraintViolations = validationException.getConstraintViolations();
        List<String> messages = constraintViolations.stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(ErrorResponseDto.builder()
                .errorCode(ErrorCode.DATA_NOT_VALID)
                .errorMessage(String.join(", ", messages))
                .build());
    }

}
