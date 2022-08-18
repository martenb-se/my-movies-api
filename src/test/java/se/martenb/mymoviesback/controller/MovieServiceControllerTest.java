package se.martenb.mymoviesback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.martenb.mymoviesback.model.Movie;
import se.martenb.mymoviesback.service.MovieService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MovieServiceController.class)
class MovieServiceControllerTest {

    @MockBean
    MovieService movieService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetAllMovies() throws Exception {
        Movie movieSpiderNo = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        Movie movieSpiderFar = new Movie("tt6320628", "Spider-Man: Far from Home", true, 9);
        Movie movieSpiderHome = new Movie("tt2250912", "Spider-Man: Homecoming", true, 8);
        List<Movie> movies = List.of(movieSpiderNo, movieSpiderFar, movieSpiderHome);

        when(movieService.getAllMovies(null)).thenReturn(movies);

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Spider-Man: No Way Home")))
                .andExpect(jsonPath("$[1].name", Matchers.is("Spider-Man: Far from Home")))
                .andExpect(jsonPath("$[2].name", Matchers.is("Spider-Man: Homecoming")));
    }

    @Test
    public void testGetAllMoviesNothingAdded() throws Exception {
        List<Movie> movies = Collections.emptyList();

        when(movieService.getAllMovies(null)).thenReturn(movies);

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddMovie() throws Exception {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        String movieAsJson = new ObjectMapper().writeValueAsString(movie);
        String url = "/api/movies";

        doNothing().when(movieService).addMovie(isA(Movie.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertEquals("Movie is saved successfully", mvcResult.getResponse().getContentAsString());
        verify(movieService, times(1)).addMovie(refEq(movie));

    }

    @Test
    public void testUpdateMovie() throws Exception {
        String imdbId = "tt10872600";
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home (NEW)", true, 10);
        String movieAsJson = new ObjectMapper().writeValueAsString(movie);
        String url = "/api/movies/" + imdbId;

        doNothing().when(movieService).updateMovie(isA(String.class), isA(Movie.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("Movie was updated successfully", mvcResult.getResponse().getContentAsString());
        verify(movieService, times(1)).updateMovie(eq(imdbId), refEq(movie));

    }

    @Test
    public void testUpdateMovieRating() throws Exception {
        String imdbId = "tt10872600";
        int rating = 10;
        String movieAsJson = new ObjectMapper().writeValueAsString(rating);
        String url = "/api/movies/" + imdbId + "/rating";

        doNothing().when(movieService).updateRating(isA(String.class), isA(Integer.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("Movie rating was updated successfully", mvcResult.getResponse().getContentAsString());
        verify(movieService, times(1)).updateRating(eq(imdbId), eq(rating));

    }

    @Test
    public void testUpdateMovieName() throws Exception {
        String imdbId = "tt10872600";
        String movieAsString = "Spider-Man: No Way Home (NEW)";
        String movieAsJson = "\"" + movieAsString + "\"";
        String url = "/api/movies/" + imdbId + "/name";

        doNothing().when(movieService).updateName(isA(String.class), isA(String.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("Movie name was updated successfully", mvcResult.getResponse().getContentAsString());
        verify(movieService, times(1)).updateName(eq(imdbId), eq(movieAsString));
    }

    @Test
    public void testUpdateMovieImdbId() throws Exception {
        String imdbIdAsString = "tt10872600";
        String newImdbIdAsString = "tt99999999";
        String newImdbIdAsJSON = "\"" + newImdbIdAsString + "\"";

        String url = "/api/movies/" + imdbIdAsString + "/imdbid";

        doNothing().when(movieService).updateImdbId(isA(String.class), isA(String.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newImdbIdAsJSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("Movie IMDB id was updated successfully",
                mvcResult.getResponse().getContentAsString());
        verify(movieService, times(1)).updateImdbId(eq(imdbIdAsString), eq(newImdbIdAsString));
    }

    @Test
    public void testUpdateMovieSetSeen() throws Exception {
        String imdbId = "tt10872600";
        int rating = 10;
        String movieAsJson = new ObjectMapper().writeValueAsString(rating);
        String url = "/api/movies/" + imdbId + "/seen";

        doNothing().when(movieService).setMovieSeen(isA(String.class), isA(Integer.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieAsJson);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("Movie seen status and rating updated successfully",
                mvcResult.getResponse().getContentAsString());
        verify(movieService, times(1)).setMovieSeen(eq(imdbId), eq(rating));
    }

    @Test
    public void testUpdateMovieSetUnseen() throws Exception {
        String imdbId = "tt10872600";
        String url = "/api/movies/" + imdbId + "/unseen";

        doNothing().when(movieService).setMovieUnseen(isA(String.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("Movie seen status and rating removed successfully",
                mvcResult.getResponse().getContentAsString());
        verify(movieService, times(1)).setMovieUnseen(eq(imdbId));
    }

    @Test
    public void testDeleteMovie() throws Exception {
        String imdbId = "tt10872600";
        String url = "/api/movies/" + imdbId;

        doNothing().when(movieService).deleteMovie(isA(String.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("Movie is deleted successfully", mvcResult.getResponse().getContentAsString());
        verify(movieService, times(1)).deleteMovie(eq(imdbId));

    }

}