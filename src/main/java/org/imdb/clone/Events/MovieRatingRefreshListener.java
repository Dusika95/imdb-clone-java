package org.imdb.clone.Events;

import org.imdb.clone.models.Movie;
import org.imdb.clone.models.Rating;
import org.imdb.clone.repository.MovieRepository;
import org.imdb.clone.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieRatingRefreshListener implements ApplicationListener<MovieRatingRefreshEvent> {

    RatingRepository ratingRepository;
    MovieRepository movieRepository;
    @Autowired
    public MovieRatingRefreshListener(MovieRepository movieRepository,RatingRepository ratingRepository) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
    }

    @EventListener
    @Override
    public void onApplicationEvent(MovieRatingRefreshEvent event) {
        Movie movie = null;


        try {
            movie = movieRepository.findById(event.getMovieId()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        List<Rating> ratingsOfMovie = ratingRepository.findByMovieId(event.getMovieId());

        int sumScore = ratingsOfMovie.stream()
                .mapToInt(Rating::getScore)
                .sum();

        if (ratingsOfMovie.size() > 0) {
            movie.setRating((double) sumScore / ratingsOfMovie.size());
        } else {
            movie.setRating(0.0);
        }

        movieRepository.save(movie);
    }
}
