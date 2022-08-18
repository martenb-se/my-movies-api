package se.martenb.mymoviesback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.martenb.mymoviesback.controller.MovieAlreadyExistsException;
import se.martenb.mymoviesback.controller.MovieNotFoundException;
import se.martenb.mymoviesback.dao.MovieRepository;
import se.martenb.mymoviesback.model.Movie;

import java.util.ArrayList;
import java.util.List;


@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Retrieve a list of all movies, if name is set, then all movies containing the requested name will be returned.
     *
     * @return A list of movies if found, otherwise an empty list is returned.
     */
    @Override
    public List<Movie> getAllMovies(String name) {
        List<Movie> movies = new ArrayList<Movie>();
        if (name == null)
            movies.addAll(movieRepository.findAll());
        else
            movies.addAll(movieRepository.findByNameContaining(name));
        return movies;
    }

    /**
     * Retrieve a movie by its IMDB id
     *
     * @param imdbId The IMDB id of the movie to retrieve.
     * @return If the movie exists it is returned, otherwise null.
     */
    @Override
    public Movie getMovieByImdbId(String imdbId) {
        return movieRepository.findOneByImdbId(imdbId);
    }

    /**
     * Add a movie to the personal collection.
     *
     * @param movie The movie to add.
     */
    @Override
    public void addMovie(Movie movie) {
        if(movieRepository.findOneByImdbId(movie.getImdbId()) != null) throw new MovieAlreadyExistsException();
        movieRepository.save(movie);
    }

    /**
     * Update a movie given by the specified IMDB id.
     *
     * @param imdbId The IMDB id of the movie to update.
     * @param movie The new details for the updated movie.
     */
    @Override
    public void updateMovie(String imdbId, Movie movie) {
        Movie movieToUpdate = movieRepository.findOneByImdbId(imdbId);
        if(movieToUpdate == null) throw new MovieNotFoundException();

        movieToUpdate.updateMovie(movie);
        movieRepository.save(movieToUpdate);
    }

    /**
     * Update a movie's rating given by its IMDB id.
     *
     * @param imdbId The IMDB id of the movie to update.
     * @param rating The new rating for the updated movie.
     */
    public void updateRating(String imdbId, int rating) {
        Movie movieToUpdate = movieRepository.findOneByImdbId(imdbId);
        if(movieToUpdate == null) throw new MovieNotFoundException();

        movieToUpdate.setRating(rating);
        movieRepository.save(movieToUpdate);
    }

    /**
     * Update a movie's name given by its IMDB id.
     *
     * @param imdbId The IMDB id of the movie to update.
     * @param name The new name for the updated movie.
     */
    @Override
    public void updateName(String imdbId, String name) {
        Movie movieToUpdate = movieRepository.findOneByImdbId(imdbId);
        if(movieToUpdate == null) throw new MovieNotFoundException();

        movieToUpdate.setName(name);
        movieRepository.save(movieToUpdate);
    }

    /**
     * Update a movie's IMDB id given by its current IMDB id.
     *
     * @param imdbId The IMDB id of the movie to update.
     * @param imdbIdNew The new IMDB id for the updated movie.
     */
    @Override
    public void updateImdbId(String imdbId, String imdbIdNew) {
        Movie movieToUpdate = movieRepository.findOneByImdbId(imdbId);
        if(movieToUpdate == null) throw new MovieNotFoundException();

        movieToUpdate.setImdbId(imdbIdNew);
        movieRepository.save(movieToUpdate);
    }

    /**
     * Update a movie's status as having been seen and give the movie a rating.
     *
     * @param imdbId The IMDB id of the movie to update.
     * @param rating The new rating for the updated movie.
     */
    @Override
    public void setMovieSeen(String imdbId, int rating) {
        Movie movieToUpdate = movieRepository.findOneByImdbId(imdbId);
        if(movieToUpdate == null) throw new MovieNotFoundException();

        movieToUpdate.setSeen(true);
        movieToUpdate.setRating(rating);
        movieRepository.save(movieToUpdate);
    }

    /**
     * Update a movie's status as not having been seen and reset any previous rating on the movie.
     *
     * @param imdbId The IMDB id of the movie to update.
     */
    @Override
    public void setMovieUnseen(String imdbId) {
        Movie movieToUpdate = movieRepository.findOneByImdbId(imdbId);
        if(movieToUpdate == null) throw new MovieNotFoundException();

        movieToUpdate.setSeen(false);
        movieToUpdate.setRating(0);
        movieRepository.save(movieToUpdate);
    }

    /**
     * Delete a movie from the personal collection.
     *
     * @param imdbId The IMDB id of the movie to delete.
     */
    @Override
    public void deleteMovie(String imdbId) {
        Movie movieToDelete = movieRepository.findOneByImdbId(imdbId);
        if(movieToDelete == null) throw new MovieNotFoundException();

        movieRepository.deleteById(movieToDelete.getId());
    }
}
