package se.martenb.mymoviesback.service;

import se.martenb.mymoviesback.model.Movie;

import java.util.List;

public interface MovieService {
    public abstract List<Movie> getAllMovies(String name);
    public abstract Movie getMovieByImdbId(String imdbId);
    public abstract void addMovie(Movie movie);
    public abstract void updateMovie(String imdbId, Movie movie);
    public abstract void updateRating(String imdbId, int rating);
    public abstract void updateName(String imdbId, String name);
    public abstract void updateImdbId(String imdbId, String imdbIdNew);
    public abstract void setMovieSeen(String imdbId, int rating);
    public abstract void setMovieUnseen(String imdbId);
    public abstract void deleteMovie(String imdbId);
}
