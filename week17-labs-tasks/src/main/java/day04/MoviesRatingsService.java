package day04;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;

public class MoviesRatingsService {
    private MoviesRepository moviesRepository;
    private RatingRepository ratingRepository;

    public MoviesRatingsService(MoviesRepository moviesRepository, RatingRepository ratingRepository) {
        this.moviesRepository = moviesRepository;
        this.ratingRepository = ratingRepository;
    }

    public void addRatings(String title, Integer... ratings) {
        Optional<Movie> movieResult = moviesRepository.findMovieByTitle(title);
        if (movieResult.isPresent()) {
            long movieId = movieResult.get().getId();
            ratingRepository.insertRating(movieId, Arrays.asList(ratings));
        }
        else {
            throw new IllegalArgumentException("Can't find movie");
        }
    }
}
