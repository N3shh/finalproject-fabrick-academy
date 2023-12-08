package it.fabrick.finalproject.service;

import it.fabrick.finalproject.enumeration.ErrorCode;
import it.fabrick.finalproject.exception.BadRequestException;
import it.fabrick.finalproject.exception.DataNotValidException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
public class ValidationService {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public void doValidate(Object o) {
        Set<ConstraintViolation<Object>> constraintViolations = validatorFactory.getValidator().validate(o);
        if (!constraintViolations.isEmpty()) {
            throw new DataNotValidException(ErrorCode.DATA_NOT_VALID, constraintViolations);
        }
    }

    public void doValidateIntegerAsString(String number) {
        try {
            Integer.parseInt(number);
        } catch (Exception e) {
            throw new BadRequestException(String.format("number '%s' not valid", number),
                    e, ErrorCode.DATA_NOT_VALID);
        }
    }
}
