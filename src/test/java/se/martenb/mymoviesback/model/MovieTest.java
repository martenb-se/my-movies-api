package se.martenb.mymoviesback.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testCorrectEntry() {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertEquals(0, violations.size());
    }

    @Test
    public void testImdbIdNull() {
        Movie movie = new Movie(null, "Spider-Man: No Way Home", true, 10);

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertEquals(1, violations.size());
        assertEquals("IMDB id cannot be null or blank", violations.iterator().next().getMessage());
    }

    @Test
    public void testImdbIdBlank() {
        Movie movie = new Movie("", "Spider-Man: No Way Home", true, 10);

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertEquals(2, violations.size());
        Assertions.assertThat(violations).extracting(ConstraintViolation::getMessage).containsAll(
                Arrays.asList(
                    "IMDB id cannot be null or blank",
                    "IMDB id must be between 1 and 20 characters"));

    }

    @Test
    public void testImdbIdTooLong() {
        Movie movie = new Movie("tt9999999999999999999", "Spider-Man: No Way Home", true, 10);

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertEquals(1, violations.size());
        assertEquals("IMDB id must be between 1 and 20 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void testNameNull() {
        Movie movie = new Movie("tt10872600", null, true, 10);

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertEquals(1, violations.size());
        assertEquals("Name cannot be null or blank", violations.iterator().next().getMessage());
    }

    @Test
    public void testNameBlank() {
        Movie movie = new Movie("tt10872600", "", true, 10);

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertEquals(2, violations.size());
        Assertions.assertThat(violations).extracting(ConstraintViolation::getMessage).containsAll(
                Arrays.asList(
                        "Name cannot be null or blank",
                        "Name must be between 1 and 255 characters"));
    }

    @Test
    public void testNameTooLong() {
        Movie movie = new Movie("tt10872600", "X".repeat(256), true, 10);

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertEquals(1, violations.size());
        assertEquals("Name must be between 1 and 255 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void testSeenFalseRatingSet() {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", false, 10);

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertEquals(1, violations.size());
        assertEquals(
                "If movie is not seen then rating cannot be set and if movie is seen then rating must be between 1 and 10",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testSeenTrueRatingTooLow() {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 0);

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertEquals(1, violations.size());
        assertEquals(
                "If movie is not seen then rating cannot be set and if movie is seen then rating must be " +
                        "between 1 and 10",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testSeenTrueRatingTooHigh() {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 11);

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertEquals(1, violations.size());
        assertEquals(
                "If movie is not seen then rating cannot be set and if movie is seen then rating must be " +
                        "between 1 and 10",
                violations.iterator().next().getMessage());
    }

}