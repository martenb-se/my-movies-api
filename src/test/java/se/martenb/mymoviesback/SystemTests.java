package se.martenb.mymoviesback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.martenb.mymoviesback.model.Movie;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@EnabledIfEnvironmentVariable(named = "SPRING_TESTING_PROFILES_ACTIVE", matches = ".*systemtest.*")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SystemTests {

    @LocalServerPort
    private int port;

    private String urlBase;

    @BeforeEach
    public void setUp() {
        urlBase = "http://localhost:" + port + "/api/movies/";
    }

    @Test
    public void testAddGetDelete() {
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        Assertions.assertThat(restTemplate.postForEntity(urlBase, movie, String.class).getBody())
                .isEqualTo("Movie is saved successfully");

        Movie[] movies = restTemplate.getForObject(urlBase, Movie[].class);
        Assertions.assertThat(movies).extracting(Movie::getImdbId).containsOnly(movie.getImdbId());

        restTemplate.delete(urlBase + movie.getImdbId());
        Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
    }

    @Test
    public void testAddImdbIdNull() {
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = new Movie(
                null,
                "Spider-Man: No Way Home",
                true,
                10);

        assertThatThrownBy(() -> restTemplate.postForEntity(urlBase, movie, String.class))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("IMDB id cannot be null or blank");

        Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
    }

    @Test
    public void testAddImdbIdTooShort() {
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = new Movie(
                "",
                "Spider-Man: No Way Home",
                true,
                10);

        assertThatThrownBy(() -> restTemplate.postForEntity(urlBase, movie, String.class))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("IMDB id must be between 1 and 20 characters");

        Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
    }

    @Test
    public void testAddImdbIdTooLong() {
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = new Movie(
                "tt999999999999999999999999999999999999999999",
                "Spider-Man: No Way Home",
                true,
                10);

        assertThatThrownBy(() -> restTemplate.postForEntity(urlBase, movie, String.class))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("IMDB id must be between 1 and 20 characters");

        Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
    }

    @Test
    public void testAddNameNull() {
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = new Movie(
                "tt10872600",
                null,
                true,
                10);

        assertThatThrownBy(() -> restTemplate.postForEntity(urlBase, movie, String.class))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("Name cannot be null or blank");

        Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
    }

    @Test
    public void testAddNameTooShort() {
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = new Movie(
                "tt10872600",
                "",
                true,
                10);

        assertThatThrownBy(() -> restTemplate.postForEntity(urlBase, movie, String.class))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("Name cannot be null or blank");

        Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
    }

    @Test
    public void testAddNameTooLong() {
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = new Movie(
                "tt10872600",
                "X".repeat(256),
                true,
                10);

        assertThatThrownBy(() -> restTemplate.postForEntity(urlBase, movie, String.class))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("Name must be between 1 and 255 characters");

        Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
    }

    @Test
    public void testAddSeenFalseRatingSet() {
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", false, 10);

        assertThatThrownBy(() -> restTemplate.postForEntity(urlBase, movie, String.class))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("If movie is not seen then rating cannot be set and if movie is seen " +
                        "then rating must be between 1 and 10");

        Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
    }

    @Test
    public void testAddSeenTrueRatingTooLow() {
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 0);

        assertThatThrownBy(() -> restTemplate.postForEntity(urlBase, movie, String.class))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("If movie is not seen then rating cannot be set and if movie is seen " +
                        "then rating must be between 1 and 10");

        Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
    }

    @Test
    public void testAddSeenTrueRatingTooHigh() {
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 11);

        assertThatThrownBy(() -> restTemplate.postForEntity(urlBase, movie, String.class))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("If movie is not seen then rating cannot be set and if movie is seen " +
                        "then rating must be between 1 and 10");

        Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
    }

    @Nested
    class TestsWithMovies{
        private final RestTemplate restTemplate = new RestTemplate();

        private final Movie movieSpiderNo =
                new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        private final Movie movieSpiderFar =
                new Movie("tt6320628", "Spider-Man: Far from Home", true, 9);
        private final Movie movieSpiderHome =
                new Movie("tt2250912", "Spider-Man: Homecoming", true, 8);
        private final Movie movieAvengerEnd =
                new Movie("tt4154796", "Avengers: Endgame", true, 9);
        private final Movie movieAvengerInf =
                new Movie("tt4154756", "Avengers: Infinity War", false, 0);

        @BeforeEach
        public void setUp() {
            Assertions.assertThat(restTemplate.postForEntity(urlBase, movieSpiderNo, String.class).getBody())
                    .isEqualTo("Movie is saved successfully");
            Assertions.assertThat(restTemplate.postForEntity(urlBase, movieSpiderFar, String.class).getBody())
                    .isEqualTo("Movie is saved successfully");
            Assertions.assertThat(restTemplate.postForEntity(urlBase, movieSpiderHome, String.class).getBody())
                    .isEqualTo("Movie is saved successfully");
            Assertions.assertThat(restTemplate.postForEntity(urlBase, movieAvengerEnd, String.class).getBody())
                    .isEqualTo("Movie is saved successfully");
            Assertions.assertThat(restTemplate.postForEntity(urlBase, movieAvengerInf, String.class).getBody())
                    .isEqualTo("Movie is saved successfully");
        }

        @AfterEach
        public void tearDown() {
            restTemplate.delete(urlBase + "/" + movieSpiderNo.getImdbId());
            restTemplate.delete(urlBase + "/" + movieSpiderFar.getImdbId());
            restTemplate.delete(urlBase + "/" + movieSpiderHome.getImdbId());
            restTemplate.delete(urlBase + "/" + movieAvengerEnd.getImdbId());
            restTemplate.delete(urlBase + "/" + movieAvengerInf.getImdbId());
            Assertions.assertThat(restTemplate.getForObject(urlBase, Movie[].class)).isNull();
        }

        @Test
        public void testGetAllMovies() {
            Movie[] movies = restTemplate.getForObject(urlBase, Movie[].class);
            Assertions.assertThat(movies).extracting(Movie::getImdbId).containsAll(
                    Arrays.asList(
                            movieSpiderNo.getImdbId(),
                            movieSpiderFar.getImdbId(),
                            movieSpiderHome.getImdbId(),
                            movieAvengerEnd.getImdbId(),
                            movieAvengerInf.getImdbId()));
        }

        @Test
        public void testUpdateMovie() {
            String movieImdbIdToUpdate = "tt10872600";
            Movie movieUpdate = new
                    Movie("tt10872600", "Spider-Man: No Way Home (NEW)", false, 0);

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate;
            HttpHeaders httpHeaders = restTemplate.headForHeaders(urlUpdate);
            HttpEntity<Movie> requestUpdate = new HttpEntity<>(movieUpdate, httpHeaders);
            ResponseEntity<String> response =
                    restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class);
            Assertions.assertThat(response.getBody()).contains("Movie was updated successfully");

            urlUpdate = urlBase + "/" + movieUpdate.getImdbId();
            Movie updatedMovie = restTemplate.getForObject(urlUpdate, Movie.class);
            assert updatedMovie != null;
            Assertions.assertThat(updatedMovie.getImdbId()).isEqualTo(movieUpdate.getImdbId());
            Assertions.assertThat(updatedMovie.getName()).isEqualTo(movieUpdate.getName());
            Assertions.assertThat(updatedMovie.isSeen()).isEqualTo(movieUpdate.isSeen());
            Assertions.assertThat(updatedMovie.getRating()).isEqualTo(movieUpdate.getRating());
        }

        @Test
        public void testUpdateMovieNotFound() {
            String movieImdbIdToUpdate = "tt99999999";
            Movie movieUpdate = new
                    Movie("tt10872600", "Spider-Man: No Way Home (NEW)", false, 0);

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate;
            HttpHeaders httpHeaders = restTemplate.headForHeaders(urlUpdate);
            HttpEntity<Movie> requestUpdate = new HttpEntity<>(movieUpdate, httpHeaders);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("Movie not found");
        }

        @Test
        public void testUpdateMovieRating() throws Exception {
            String movieImdbIdToUpdate = "tt10872600";
            int newRating = 5;
            String movieAsJson = new ObjectMapper().writeValueAsString(newRating);

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/rating";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(movieAsJson, headers);
            ResponseEntity<String> response =
                    restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class);

            Assertions.assertThat(response.getBody()).contains("Movie rating was updated successfully");

            Movie updatedMovie = restTemplate.getForObject(urlBase + "/" + movieImdbIdToUpdate, Movie.class);
            assert updatedMovie != null;
            Assertions.assertThat(updatedMovie.getRating()).isEqualTo(newRating);
        }

        @Test
        public void testUpdateMovieRatingMovieNotFound() throws Exception {
            String movieImdbIdToUpdate = "tt99999999";
            int newRating = 0;
            String movieAsJson = new ObjectMapper().writeValueAsString(newRating);

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/rating";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(movieAsJson, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("Movie not found");
        }

        @Test
        public void testUpdateMovieRatingTooLow() throws Exception {
            String movieImdbIdToUpdate = "tt10872600";
            int newRating = 0;
            String movieAsJson = new ObjectMapper().writeValueAsString(newRating);

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/rating";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(movieAsJson, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("If movie is not seen then rating cannot be set and if movie is seen " +
                            "then rating must be between 1 and 10");
        }

        @Test
        public void testUpdateMovieRatingTooHigh() throws Exception {
            String movieImdbIdToUpdate = "tt10872600";
            int newRating = 11;
            String movieAsJson = new ObjectMapper().writeValueAsString(newRating);

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/rating";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(movieAsJson, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("If movie is not seen then rating cannot be set and if movie is seen " +
                            "then rating must be between 1 and 10");
        }

        @Test
        public void testUpdateMovieNameWrongEndpoint() {
            String movieImdbIdToUpdate = "tt10872600";
            String newName = "Random Name";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/rating";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(newName, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("Bad Request");
        }

        @Test
        public void testUpdateMovieName() {
            String movieImdbIdToUpdate = "tt10872600";
            String newName = "Random Name";
            String newNameAsJson = "\"" + newName + "\"";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/name";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(newNameAsJson, headers);
            ResponseEntity<String> response =
                    restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class);

            Assertions.assertThat(response.getBody()).contains("Movie name was updated successfully");

            Movie updatedMovie = restTemplate.getForObject(urlBase + "/" + movieImdbIdToUpdate, Movie.class);
            assert updatedMovie != null;
            Assertions.assertThat(updatedMovie.getName()).isEqualTo(newName);
        }

        @Test
        public void testUpdateMovieNameNull() {
            String movieImdbIdToUpdate = "tt10872600";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/name";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(null, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("Bad Request");
        }

        @Test
        public void testUpdateMovieNameTooShort() {
            String movieImdbIdToUpdate = "tt10872600";
            String newName = "";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/name";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(newName, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("Bad Request");
        }

        @Test
        public void testUpdateMovieNameTooLong() {
            String movieImdbIdToUpdate = "tt10872600";
            String newName = "X".repeat(256);
            String newNameAsJSON = "\"" + newName + "\"";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/name";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(newNameAsJSON, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("Name must be between 1 and 255 characters");
        }

        @Test
        public void testUpdateMovieImdbIdAndRestore() {
            String movieImdbIdToUpdate = "tt10872600";
            String movieImdbIdToUpdateAsJSON = "\"" + movieImdbIdToUpdate + "\"";
            String newImdbId = "tt99999999";
            String newImdbIdAsJSON = "\"" + newImdbId + "\"";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/imdbid";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(newImdbIdAsJSON, headers);
            ResponseEntity<String> response =
                    restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class);

            Assertions.assertThat(response.getBody()).contains("Movie IMDB id was updated successfully");

            Movie updatedMovie = restTemplate.getForObject(urlBase + "/" + newImdbId, Movie.class);
            assert updatedMovie != null;
            Assertions.assertThat(updatedMovie.getImdbId()).isEqualTo(newImdbId);

            // Restore
            requestUpdate = new HttpEntity<>(movieImdbIdToUpdateAsJSON, headers);
            response = restTemplate.exchange(urlBase + "/" + newImdbId + "/imdbid", HttpMethod.PUT, requestUpdate, String.class);
            Assertions.assertThat(response.getBody()).contains("Movie IMDB id was updated successfully");
            updatedMovie = restTemplate.getForObject(urlBase + "/" + movieImdbIdToUpdate, Movie.class);
            assert updatedMovie != null;
            Assertions.assertThat(updatedMovie.getImdbId()).isEqualTo(movieImdbIdToUpdate);
        }

        @Test
        public void testUpdateMovieImdbIdNull() {
            String movieImdbIdToUpdate = "tt10872600";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/imdbid";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(null, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("Bad Request");
        }

        @Test
        public void testUpdateMovieImdbIdTooShort() {
            String movieImdbIdToUpdate = "tt10872600";
            String newImdbId = "";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/imdbid";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(newImdbId, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("Bad Request");
        }

        @Test
        public void testUpdateMovieImdbIdTooLong() {
            String movieImdbIdToUpdate = "tt10872600";
            String newImdbId = "tt9999999999999999999";
            String newImdbIdAsJSON = "\"" + newImdbId + "\"";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/imdbid";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(newImdbIdAsJSON, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("IMDB id must be between 1 and 20 characters");
        }

        @Test
        public void testUpdateMovieSeen() {
            String movieImdbIdToUpdate = "tt4154756";
            int newRating = 8;

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/seen";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<Integer> requestUpdate = new HttpEntity<>(newRating, headers);
            ResponseEntity<String> response =
                    restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class);

            Assertions.assertThat(response.getBody()).contains("Movie seen status and rating updated successfully");

            Movie updatedMovie = restTemplate.getForObject(urlBase + "/" + movieImdbIdToUpdate, Movie.class);
            assert updatedMovie != null;
            Assertions.assertThat(updatedMovie.isSeen()).isTrue();
            Assertions.assertThat(updatedMovie.getRating()).isEqualTo(newRating);
        }

        @Test
        public void testUpdateMovieSeenNullRating() {
            String movieImdbIdToUpdate = "tt10872600";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/seen";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> requestUpdate = new HttpEntity<>(null, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining("Bad Request");
        }

        @Test
        public void testUpdateMovieSeenTooLowRating() {
            String movieImdbIdToUpdate = "tt10872600";
            int newRating = 0;

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/seen";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<Integer> requestUpdate = new HttpEntity<>(newRating, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining(
                            "If movie is not seen then rating cannot be set and if movie is seen then " +
                                    "rating must be between 1 and 10");
        }

        @Test
        public void testUpdateMovieSeenTooHighRating() {
            String movieImdbIdToUpdate = "tt10872600";
            int newRating = 11;

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/seen";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<Integer> requestUpdate = new HttpEntity<>(newRating, headers);
            assertThatThrownBy(() -> restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .hasMessageContaining(
                            "If movie is not seen then rating cannot be set and if movie is seen then " +
                                    "rating must be between 1 and 10");
        }

        @Test
        public void testUpdateMovieUnseen() {
            String movieImdbIdToUpdate = "tt10872600";

            String urlUpdate = urlBase + "/" + movieImdbIdToUpdate + "/unseen";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
            HttpEntity<Integer> requestUpdate = new HttpEntity<>(null, headers);
            ResponseEntity<String> response =
                    restTemplate.exchange(urlUpdate, HttpMethod.PUT, requestUpdate, String.class);

            Assertions.assertThat(response.getBody()).contains("Movie seen status and rating removed successfully");

            Movie updatedMovie = restTemplate.getForObject(urlBase + "/" + movieImdbIdToUpdate, Movie.class);
            assert updatedMovie != null;
            Assertions.assertThat(updatedMovie.isSeen()).isFalse();
            Assertions.assertThat(updatedMovie.getRating()).isEqualTo(0);
        }

    }

}
