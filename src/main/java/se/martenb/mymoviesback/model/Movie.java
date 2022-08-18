package se.martenb.mymoviesback.model;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A representation of a movie.
 */
@Entity
@SeenAndRatingAnnotation(message = "If movie is not seen then rating cannot be set and if movie is seen then rating " +
        "must be between 1 and 10")
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "IMDB id cannot be null or blank")
    @Size(min = 1, max = 20, message = "IMDB id must be between 1 and 20 characters")
    @Column(name = "imdb_id")
    private String imdbId;
    @NotBlank(message = "Name cannot be null or blank")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    @Column(name = "name")
    private String name;
    @Column(name = "seen")
    private boolean seen;
    @Column(name = "rating")
    private int rating;

    public Movie() {}

    /**
     * A representation of a movie with an IMDB id, a name, if it has been seen and a rating.
     *
     * @param imdbId The IMDB id for the movie.
     * @param name The name of the movie.
     * @param seen If the moves has been seen or not.
     * @param rating Rating of the movie if it has been seen.
     */
    public Movie(String imdbId, String name, boolean seen, int rating) {
        this.imdbId = imdbId;
        this.name = name;
        this.seen = seen;
        this.rating = rating;
    }

    /**
     * Get the unique ID for the movie entry.
     *
     * @return The unique ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Get the unique IMDB id for the movie entry.
     *
     * @return The unique IMDB id.
     */
    public String getImdbId() {
        return imdbId;
    }

    /**
     * Get the name of the movie entry.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get whether the movie has been seen or not.
     *
     * @return True if the movie has been see, otherwise false.
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     * Get the rating of the movie entry.
     *
     * @return The rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Set the IMDB id for the movie entry.
     *
     * @param imdbId The new IMDB id.
     */
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    /**
     * Set the name for the movie entry.
     *
     * @param name The new name of the movie.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set if the movie has been seen.
     *
     * @param seen True if the movie has been seen, false otherwise.
     */
    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    /**
     * Set the rating of the movie.
     *
     * @param rating The new rating for the movie.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Update the current movie entry using another movie entry. All values will be transferred except the other
     * entry's id.
     *
     * @param movie The movie entry to get data from.
     */
    public void updateMovie(Movie movie) {
        this.imdbId = movie.getImdbId();
        this.name = movie.getName();
        this.seen = movie.isSeen();
        this.rating = movie.getRating();
    }

    /**
     * Get a string representation of the movie entry.
     *
     * @return The string representation of the whole entry.
     */
    @Override
    public String toString() {
        return "Movies [id=" + id + ", imdb_id=" + imdbId + ", name=" + name + ", seen=" + seen + ", rating=" + rating + "]";
    }
}
