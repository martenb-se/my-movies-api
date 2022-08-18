package se.martenb.mymoviesback.model;

import se.martenb.mymoviesback.model.SeenAndRatingAnnotation;
import se.martenb.mymoviesback.model.Movie;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SeenAndRatingValidator implements ConstraintValidator<SeenAndRatingAnnotation, Movie> {
    public void initialize(SeenAndRatingAnnotation constraintAnnotation) {}

    public boolean isValid(Movie object, ConstraintValidatorContext context) {
        if (object == null) {
            throw new IllegalArgumentException("@SeenAndRatingAnnotation only applies to Movie objects");
        }
        boolean seen = ((Movie) object).isSeen();
        int rating = ((Movie) object).getRating();

        return seen && rating >= 1 && rating <= 10 || !seen && rating == 0;
    }
}
