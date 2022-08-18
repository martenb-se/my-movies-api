package se.martenb.mymoviesback.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.martenb.mymoviesback.model.Movie;
import se.martenb.mymoviesback.service.MovieService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4173"}, maxAge = 3600)
@RestController
@RequestMapping("/api/movies")
public class MovieServiceController {
    private final MovieService movieService;

    @Autowired
    public MovieServiceController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Movie>> getAll(@RequestParam(required = false) String name) {
        List<Movie> foundMovies = movieService.getAllMovies(name);
        if (foundMovies.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(foundMovies, HttpStatus.OK);
    }

    @GetMapping(value = "{imdbId}")
    public ResponseEntity<Movie> getByImdbId(@PathVariable("imdbId") String imdbId) {
        Movie foundMovie = movieService.getMovieByImdbId(imdbId);
        if (imdbId == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(foundMovie, HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> add(@RequestBody Movie movie) {
        movieService.addMovie(movie);
        return new ResponseEntity<>("Movie is saved successfully", HttpStatus.CREATED);
    }

    @PutMapping(value = "{imdbId}")
    public ResponseEntity<Object> update(@PathVariable("imdbId") String imdbId, @RequestBody Movie movie) {
        movieService.updateMovie(imdbId, movie);
        return new ResponseEntity<>("Movie was updated successfully", HttpStatus.OK);
    }

    @PutMapping(value = "{imdbId}/rating")
    public ResponseEntity<Object> updateRating(@PathVariable("imdbId") String imdbId, @RequestBody int rating) {
        movieService.updateRating(imdbId, rating);
        return new ResponseEntity<>("Movie rating was updated successfully", HttpStatus.OK);
    }

    @PutMapping(value = "{imdbId}/name")
    public ResponseEntity<Object> updateName(@PathVariable("imdbId") String imdbId, @RequestBody String name) {
        String nameValue;
        try {
            nameValue = new ObjectMapper().readValue(name, String.class);
            movieService.updateName(imdbId, nameValue);
            return new ResponseEntity<>("Movie name was updated successfully", HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("There was an error trying to process the JSON data", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "{imdbId}/imdbid")
    public ResponseEntity<Object> updateImdbId(@PathVariable("imdbId") String imdbId, @RequestBody String newImdbId) {
        String newImdbIdValue;
        try {
            newImdbIdValue = new ObjectMapper().readValue(newImdbId, String.class);
            movieService.updateImdbId(imdbId, newImdbIdValue);
            return new ResponseEntity<>("Movie IMDB id was updated successfully", HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("There was an error trying to process the JSON data", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "{imdbId}/seen")
    public ResponseEntity<Object> updateSetSeen(@PathVariable("imdbId") String imdbId, @RequestBody int rating) {
        movieService.setMovieSeen(imdbId, rating);
        return new ResponseEntity<>("Movie seen status and rating updated successfully", HttpStatus.OK);
    }

    @PutMapping(value = "{imdbId}/unseen")
    public ResponseEntity<Object> updateSetUnseen(@PathVariable("imdbId") String imdbId) {
        movieService.setMovieUnseen(imdbId);
        return new ResponseEntity<>("Movie seen status and rating removed successfully", HttpStatus.OK);
    }

    @DeleteMapping(value = "{imdbId}")
    public ResponseEntity<Object> delete(@PathVariable("imdbId") String imdbId) {
        movieService.deleteMovie(imdbId);
        return new ResponseEntity<>("Movie is deleted successfully", HttpStatus.OK);
    }
}
