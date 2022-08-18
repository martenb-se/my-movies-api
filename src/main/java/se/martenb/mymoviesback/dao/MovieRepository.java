package se.martenb.mymoviesback.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import se.martenb.mymoviesback.model.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByNameContaining(String name);
    Movie findOneByImdbId(String imdbId);
}
