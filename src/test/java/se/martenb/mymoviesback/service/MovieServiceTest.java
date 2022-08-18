package se.martenb.mymoviesback.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.martenb.mymoviesback.controller.MovieAlreadyExistsException;
import se.martenb.mymoviesback.controller.MovieNotFoundException;
import se.martenb.mymoviesback.model.Movie;
import se.martenb.mymoviesback.dao.MovieRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @InjectMocks
    MovieServiceImpl movieService;

    @Mock
    MovieRepository movieRepository;

    @Test
    public void testGetAllMovies()
    {
        Movie movieSpiderNo = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        Movie movieSpiderFar = new Movie("tt6320628", "Spider-Man: Far from Home", true, 9);
        Movie movieSpiderHome = new Movie("tt2250912", "Spider-Man: Homecoming", true, 8);
        List<Movie> movies = List.of(movieSpiderNo, movieSpiderFar, movieSpiderHome);

        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> movieList = movieService.getAllMovies(null);

        assertEquals(3, movieList.size());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllMoviesFilterByNameWay()
    {
        Movie movieSpiderNo = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        String filterText = "way";
        List<Movie> moviesFiltered = List.of(movieSpiderNo);

        when(movieRepository.findByNameContaining(filterText)).thenReturn(moviesFiltered);

        List<Movie> movieList = movieService.getAllMovies(filterText);

        assertEquals(1, movieList.size());
        verify(movieRepository, times(1)).findByNameContaining(eq(filterText));
    }

    @Test
    public void testGetMovieByImdbId()
    {
        Movie movieSpiderNo = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);

        when(movieRepository.findOneByImdbId(movieSpiderNo.getImdbId())).thenReturn(movieSpiderNo);

        Movie foundMovie = movieService.getMovieByImdbId(movieSpiderNo.getImdbId());

        assertEquals(movieSpiderNo, foundMovie);
        verify(movieRepository, times(1)).findOneByImdbId(eq(movieSpiderNo.getImdbId()));
    }

    @Test
    public void testAddMovie()
    {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);

        when(movieRepository.findOneByImdbId(movie.getImdbId())).thenReturn(null);
        when(movieRepository.save(isA(Movie.class))).thenReturn(movie);

        movieService.addMovie(movie);

        verify(movieRepository, times(1)).findOneByImdbId(eq(movie.getImdbId()));
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void testAddMovieAlreadyExisting()
    {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);

        when(movieRepository.findOneByImdbId(movie.getImdbId())).thenReturn(movie);

        assertThatThrownBy(() -> movieService.addMovie(movie))
                .isInstanceOf(MovieAlreadyExistsException.class)
                .hasNoCause();
    }

    @Test
    public void testUpdateMovie()
    {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);
        Movie newMovie = new Movie("tt10872600", "Spider-Man: No Way Home (NEW)", true, 10);

        when(movieRepository.findOneByImdbId(movie.getImdbId())).thenReturn(movie);
        when(movieRepository.save(isA(Movie.class))).thenReturn(movie);

        movieService.updateMovie(movie.getImdbId(), newMovie);

        assertEquals(movie.getName(), newMovie.getName());
        verify(movieRepository, times(1)).findOneByImdbId(eq(movie.getImdbId()));
        verify(movieRepository, times(1)).save(movie);

    }

    @Test
    public void testUpdateMovieNotExisting()
    {
        String origMovieImdbId = "tt10872600";
        Movie newMovie = new Movie("tt10872600", "Spider-Man: No Way Home (NEW)", true, 10);

        when(movieRepository.findOneByImdbId(origMovieImdbId)).thenReturn(null);

        assertThatThrownBy(() -> movieService.updateMovie(origMovieImdbId, newMovie))
                .isInstanceOf(MovieNotFoundException.class)
                .hasNoCause();

    }

    @Test
    public void testUpdateMovieRating()
    {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 8);
        int newRating = 10;

        when(movieRepository.findOneByImdbId(movie.getImdbId())).thenReturn(movie);
        when(movieRepository.save(isA(Movie.class))).thenReturn(movie);

        movieService.updateRating(movie.getImdbId(), newRating);

        assertEquals(movie.getRating(), newRating);
        verify(movieRepository, times(1)).findOneByImdbId(eq(movie.getImdbId()));
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void testUpdateMovieRatingMovieNotExisting()
    {
        String movieImdbId = "tt10872600";
        int newRating = 10;

        when(movieRepository.findOneByImdbId(movieImdbId)).thenReturn(null);

        assertThatThrownBy(() -> movieService.updateRating(movieImdbId, newRating))
                .isInstanceOf(MovieNotFoundException.class)
                .hasNoCause();
    }

    @Test
    public void testUpdateMovieName()
    {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 8);
        String newName = "Spider-Man: No Way Home (NEW)";

        when(movieRepository.findOneByImdbId(movie.getImdbId())).thenReturn(movie);
        when(movieRepository.save(isA(Movie.class))).thenReturn(movie);

        movieService.updateName(movie.getImdbId(), newName);

        assertEquals(movie.getName(), newName);
        verify(movieRepository, times(1)).findOneByImdbId(eq(movie.getImdbId()));
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void testUpdateMovieNameMovieNotExisting()
    {
        String movieImdbId = "tt10872600";
        String newName = "Spider-Man: No Way Home (NEW)";

        when(movieRepository.findOneByImdbId(movieImdbId)).thenReturn(null);

        assertThatThrownBy(() -> movieService.updateName(movieImdbId, newName))
                .isInstanceOf(MovieNotFoundException.class)
                .hasNoCause();
    }

    @Test
    public void testUpdateMovieImdbId()
    {
        String previousImdbId = "tt10872600";
        Movie movie = new Movie(previousImdbId, "Spider-Man: No Way Home", true, 8);
        String newImdbId = "tt99999999";

        when(movieRepository.findOneByImdbId(movie.getImdbId())).thenReturn(movie);
        when(movieRepository.save(isA(Movie.class))).thenReturn(movie);

        movieService.updateImdbId(movie.getImdbId(), newImdbId);

        assertEquals(movie.getImdbId(), newImdbId);
        verify(movieRepository, times(1)).findOneByImdbId(eq(previousImdbId));
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void testUpdateMovieImdbIdMovieNotExisting()
    {
        String movieImdbId = "tt10872600";
        String newImdbId = "tt99999999";

        when(movieRepository.findOneByImdbId(movieImdbId)).thenReturn(null);

        assertThatThrownBy(() -> movieService.updateImdbId(movieImdbId, newImdbId))
                .isInstanceOf(MovieNotFoundException.class)
                .hasNoCause();
    }

    @Test
    public void testUpdateMovieSetSeen()
    {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", false, 0);
        int newRating = 10;

        when(movieRepository.findOneByImdbId(movie.getImdbId())).thenReturn(movie);
        when(movieRepository.save(isA(Movie.class))).thenReturn(movie);

        movieService.setMovieSeen(movie.getImdbId(), newRating);

        assertTrue(movie.isSeen());
        assertEquals(movie.getRating(), newRating);
        verify(movieRepository, times(1)).findOneByImdbId(eq(movie.getImdbId()));
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void testUpdateMovieSetSeenMovieNotExisting()
    {
        String movieImdbId = "tt10872600";
        int newRating = 10;

        when(movieRepository.findOneByImdbId(movieImdbId)).thenReturn(null);

        assertThatThrownBy(() -> movieService.setMovieSeen(movieImdbId, newRating))
                .isInstanceOf(MovieNotFoundException.class)
                .hasNoCause();
    }

    @Test
    public void testUpdateMovieSetUnseen()
    {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);

        when(movieRepository.findOneByImdbId(movie.getImdbId())).thenReturn(movie);
        when(movieRepository.save(isA(Movie.class))).thenReturn(movie);

        movieService.setMovieUnseen(movie.getImdbId());

        assertFalse(movie.isSeen());
        assertEquals(movie.getRating(), 0);
        verify(movieRepository, times(1)).findOneByImdbId(eq(movie.getImdbId()));
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void testUpdateMovieSetUnseenMovieNotExisting()
    {
        String movieImdbId = "tt10872600";

        when(movieRepository.findOneByImdbId(movieImdbId)).thenReturn(null);

        assertThatThrownBy(() -> movieService.setMovieUnseen(movieImdbId))
                .isInstanceOf(MovieNotFoundException.class)
                .hasNoCause();
    }

    @Test
    public void testDeleteMovie()
    {
        Movie movie = new Movie("tt10872600", "Spider-Man: No Way Home", true, 10);

        when(movieRepository.findOneByImdbId(movie.getImdbId())).thenReturn(movie);
        doNothing().when(movieRepository).deleteById(eq(movie.getId()));

        movieService.deleteMovie(movie.getImdbId());

        verify(movieRepository, times(1)).findOneByImdbId(eq(movie.getImdbId()));
        verify(movieRepository, times(1)).deleteById(movie.getId());
    }

    @Test
    public void testDeleteMovieNotExisting()
    {
        String movieImdbId = "tt10872600";

        when(movieRepository.findOneByImdbId(movieImdbId)).thenReturn(null);

        assertThatThrownBy(() -> movieService.deleteMovie(movieImdbId))
                .isInstanceOf(MovieNotFoundException.class)
                .hasNoCause();
    }

}