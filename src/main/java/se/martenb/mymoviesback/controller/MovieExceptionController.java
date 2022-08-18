package se.martenb.mymoviesback.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import se.martenb.mymoviesback.MyMoviesApplication;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Set;

@ControllerAdvice
public class MovieExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(MyMoviesApplication.class);

    @ExceptionHandler(value = MovieNotFoundException.class)
    public ResponseEntity<Object> exception(MovieNotFoundException exception) {
        return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MovieAlreadyExistsException.class)
    public ResponseEntity<Object> exception(MovieAlreadyExistsException exception) {
        return new ResponseEntity<>("Movie already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> exception(ConstraintViolationException exception) {
        StringBuilder ruleViolations = new StringBuilder();
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = violations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<?> violation = iterator.next();
            ruleViolations.append(violation.getMessage());
            if (iterator.hasNext())
                ruleViolations.append(", ");
        }

        return new ResponseEntity<>("Action violated the following rule(s): " + ruleViolations, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> exception(DataIntegrityViolationException exception) {
        logger.warn("Potentially malicious action detected by user.");
        return new ResponseEntity<>("Potentially malicious action detected", HttpStatus.FORBIDDEN);
    }
}
