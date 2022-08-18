package se.martenb.mymoviesback.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.martenb.mymoviesback.model.Movie;

import java.util.Arrays;

@EnabledIfEnvironmentVariable(named = "SPRING_TESTING_PROFILES_ACTIVE", matches = ".*daotest.*")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MovieRepositoryTest {

    @Autowired
    MovieRepository movieRepository;

    @Test
    public void testCreateReadDelete() {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);

        movieRepository.save(movie);

        Iterable<Movie> movies = movieRepository.findAll();
        Assertions.assertThat(movies).extracting(Movie::getImdbId).containsOnly("tt10872600");

        movieRepository.deleteAll();
        Assertions.assertThat(movieRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindByNameContaining() {
        Movie movieSpiderNo = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        Movie movieSpiderFar = new Movie("tt6320628", "Spider-Man: Far from Home", true, 9);
        Movie movieSpiderHome = new Movie("tt2250912", "Spider-Man: Homecoming", true, 8);

        movieRepository.save(movieSpiderNo);
        movieRepository.save(movieSpiderFar);
        movieRepository.save(movieSpiderHome);

        Iterable<Movie> movies = movieRepository.findByNameContaining("Far");
        Assertions.assertThat(movies).extracting(Movie::getImdbId).containsOnly("tt6320628");

        movieRepository.deleteAll();
        Assertions.assertThat(movieRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindByNameContainingFindAll() {
        Movie movieSpiderNo = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        Movie movieSpiderFar = new Movie("tt6320628", "Spider-Man: Far from Home", true, 9);
        Movie movieSpiderHome = new Movie("tt2250912", "Spider-Man: Homecoming", true, 8);

        movieRepository.save(movieSpiderNo);
        movieRepository.save(movieSpiderFar);
        movieRepository.save(movieSpiderHome);

        Iterable<Movie> movies = movieRepository.findByNameContaining("Home");
        Assertions.assertThat(movies).extracting(Movie::getImdbId).containsAll(
                Arrays.asList("tt10872600", "tt6320628", "tt2250912"));

        movieRepository.deleteAll();
        Assertions.assertThat(movieRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindByNameContainingNoMatch() {
        Movie movieSpiderNo = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        Movie movieSpiderFar = new Movie("tt6320628", "Spider-Man: Far from Home", true, 9);
        Movie movieSpiderHome = new Movie("tt2250912", "Spider-Man: Homecoming", true, 8);

        movieRepository.save(movieSpiderNo);
        movieRepository.save(movieSpiderFar);
        movieRepository.save(movieSpiderHome);

        Iterable<Movie> movies = movieRepository.findByNameContaining("NotPresent");
        Assertions.assertThat(movies).isEmpty();

        movieRepository.deleteAll();
        Assertions.assertThat(movieRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindOneByImdbId() {
        Movie movieSpiderNo = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        Movie movieSpiderFar = new Movie("tt6320628", "Spider-Man: Far from Home", true, 9);
        Movie movieSpiderHome = new Movie("tt2250912", "Spider-Man: Homecoming", true, 8);

        movieRepository.save(movieSpiderNo);
        movieRepository.save(movieSpiderFar);
        movieRepository.save(movieSpiderHome);

        Movie movie = movieRepository.findOneByImdbId("tt2250912");
        Assertions.assertThat(movie).isEqualTo(movieSpiderHome);

        movieRepository.deleteAll();
        Assertions.assertThat(movieRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindOneByImdbIdNoMatch() {
        Movie movieSpiderNo = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        Movie movieSpiderFar = new Movie("tt6320628", "Spider-Man: Far from Home", true, 9);
        Movie movieSpiderHome = new Movie("tt2250912", "Spider-Man: Homecoming", true, 8);

        movieRepository.save(movieSpiderNo);
        movieRepository.save(movieSpiderFar);
        movieRepository.save(movieSpiderHome);

        Movie movie = movieRepository.findOneByImdbId("tt99999999");
        Assertions.assertThat(movie).isNull();

        movieRepository.deleteAll();
        Assertions.assertThat(movieRepository.findAll()).isEmpty();
    }

}