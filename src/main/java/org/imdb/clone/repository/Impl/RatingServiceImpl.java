package org.imdb.clone.repository.Impl;

import org.imdb.clone.DTOs.CurrentScoreDto;
import org.imdb.clone.DTOs.RatingCreateDto;
import org.imdb.clone.DTOs.RatingUpdateDto;
import org.imdb.clone.Events.MovieRatingRefreshEvent;
import org.imdb.clone.exceptions.DuplicateException;
import org.imdb.clone.exceptions.ForbiddenOperatorException;
import org.imdb.clone.models.Movie;
import org.imdb.clone.models.Rating;
import org.imdb.clone.models.User;
import org.imdb.clone.repository.MovieRepository;
import org.imdb.clone.repository.RatingRepository;
import org.imdb.clone.services.RatingService;
import org.imdb.clone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final MovieRepository movieRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, UserService userService, MovieRepository movieRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
        this.movieRepository = movieRepository;
        this.applicationEventPublisher= applicationEventPublisher;
    }

    @Override
    @Transactional
    public void postRating(RatingCreateDto ratingCreateDto) throws Exception {
        User user = userService.getCurrentUser();
        Movie movie = movieRepository.findById(ratingCreateDto.getMovieId()).orElseThrow(() -> new NoSuchElementException("movie not found on id: " + ratingCreateDto.getMovieId()));
        Rating rating = new Rating(ratingCreateDto.getScore(), user, movie);

        if (ratingRepository.findByUserIdAndMovieId(user.getId(), movie.getId()) != null) {
            throw new DuplicateException("User already create rating to this movie");
        }
        ratingRepository.save(rating);

        MovieRatingRefreshEvent movieRatingRefreshEvent = new MovieRatingRefreshEvent(this, ratingCreateDto.getMovieId());
        applicationEventPublisher.publishEvent(movieRatingRefreshEvent);
    }


    @Override
    @Transactional
    public void deleteRating(Long id) throws Exception {
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Rating not found with id: " + id));
        Long movieId = rating.getMovie().getId();
        ratingRepository.deleteById(rating.getMovie().getId());

        MovieRatingRefreshEvent movieRatingRefreshEvent = new MovieRatingRefreshEvent(this, movieId);
        applicationEventPublisher.publishEvent(movieRatingRefreshEvent);
    }

    @Override
    public CurrentScoreDto getRatingByUserIdAndMovieId(Long movieId) {
        User user = userService.getCurrentUser();
        Rating rating = ratingRepository.findByUserIdAndMovieId(user.getId(), movieId);
        return new CurrentScoreDto(rating.getScore());
    }

    @Override
    @Transactional
    public void updateRating(Long id, RatingUpdateDto ratingUpdateDto) throws Exception {
        User user = userService.getCurrentUser();
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new NoSuchElementException("rating not found on id: " + id));
        if (!Objects.equals(user.getId(), rating.getUser().getId())) {
            throw new ForbiddenOperatorException("you cant change that rating");
        } else {
            rating.setScore(ratingUpdateDto.getScore());
            ratingRepository.save(rating);

            MovieRatingRefreshEvent movieRatingRefreshEvent = new MovieRatingRefreshEvent(this, rating.getMovie().getId());
            applicationEventPublisher.publishEvent(movieRatingRefreshEvent);
        }
    }

}
