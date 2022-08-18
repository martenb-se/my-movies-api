package se.martenb.mymoviesback.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.martenb.mymoviesback.model.Movie;
import se.martenb.mymoviesback.dao.MovieRepository;
import se.martenb.mymoviesback.service.MovieService;
import se.martenb.mymoviesback.service.MovieServiceImpl;

@EnabledIfEnvironmentVariable(named = "SPRING_TESTING_PROFILES_ACTIVE", matches = ".*integrationtest.*")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IntegrationTests {
    @Autowired
    MovieRepository movieRepository;

    MovieService movieService;
    MovieServiceController movieServiceController;

    @BeforeEach
    void SetUp() {
        movieService = new MovieServiceImpl(movieRepository);
        movieServiceController = new MovieServiceController(movieService);
    }

    @Test
    public void testIntegrationAddAndDeleteMovie() {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);

        movieServiceController.add(movie);

        ResponseEntity<Movie> movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        Movie foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("imdbId", movie.getImdbId());

        movieServiceController.delete(movie.getImdbId());
        Assertions.assertThat(movieServiceController.getByImdbId(movie.getImdbId()).getBody()).isNull();
    }

    @Test
    public void testIntegrationAddAndUpdate() {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        Movie newMovie = new Movie("tt99999999", "Spider-Man: No Way Home (NEW)", false, 0);

        movieServiceController.add(movie);

        ResponseEntity<Movie> movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        Movie foundOriginalMovie = movieResponse.getBody();
        Assertions.assertThat(foundOriginalMovie).hasFieldOrPropertyWithValue("imdbId", movie.getImdbId());
        Assertions.assertThat(foundOriginalMovie).hasFieldOrProperty("id");

        movieServiceController.update(movie.getImdbId(), newMovie);
        movieResponse = movieServiceController.getByImdbId(newMovie.getImdbId());
        Movie foundUpdatedMovie = movieResponse.getBody();
        assert foundOriginalMovie != null;
        Assertions.assertThat(foundUpdatedMovie).hasFieldOrPropertyWithValue("id", foundOriginalMovie.getId());
    }

    @Test
    public void testIntegrationAddAndChangeRating() {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 8);
        int newRating = 10;

        movieServiceController.add(movie);

        ResponseEntity<Movie> movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        Movie foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("rating", movie.getRating());

        movieServiceController.updateRating(movie.getImdbId(), newRating);
        movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("rating", newRating);
    }

    @Test
    public void testIntegrationAddAndChangeName() throws JsonProcessingException {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 8);
        String newName = "Spider-Man: No Way Home (NEW)";

        movieServiceController.add(movie);

        ResponseEntity<Movie> movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        Movie foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("name", movie.getName());

        movieServiceController.updateName(movie.getImdbId(), new ObjectMapper().writeValueAsString(newName));
        movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("name", newName);
    }

    @Test
    public void testIntegrationAddAndChangeImdbId() throws JsonProcessingException {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 8);
        String newImdbId = "tt99999999";

        movieServiceController.add(movie);

        ResponseEntity<Movie> movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        Movie foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("name", movie.getName());

        movieServiceController.updateImdbId(movie.getImdbId(), new ObjectMapper().writeValueAsString(newImdbId));
        movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("imdbId", newImdbId);
    }

    @Test
    public void testIntegrationAddAndSetSeen() {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", false, 0);
        int newRating = 10;

        movieServiceController.add(movie);

        ResponseEntity<Movie> movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        Movie foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("name", movie.getName());

        movieServiceController.updateSetSeen(movie.getImdbId(), newRating);
        movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("seen", true);
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("rating", newRating);
    }

    @Test
    public void testIntegrationAddAndSetUnseen() {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);

        movieServiceController.add(movie);

        ResponseEntity<Movie> movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        Movie foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("name", movie.getName());

        movieServiceController.updateSetUnseen(movie.getImdbId());
        movieResponse = movieServiceController.getByImdbId(movie.getImdbId());
        foundMovie = movieResponse.getBody();
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("seen", false);
        Assertions.assertThat(foundMovie).hasFieldOrPropertyWithValue("rating", 0);
    }
}
