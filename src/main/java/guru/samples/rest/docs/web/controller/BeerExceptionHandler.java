package guru.samples.rest.docs.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class BeerExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<String> handleConstraintViolationException(ConstraintViolationException exception) {
        return exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::toString)
                .collect(toList());
    }
}
