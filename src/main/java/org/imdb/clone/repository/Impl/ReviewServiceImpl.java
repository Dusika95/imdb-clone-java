package org.imdb.clone.repository.Impl;

import org.imdb.clone.DTOs.ReviewDto;
import org.imdb.clone.DTOs.ReviewCreateDto;

import org.imdb.clone.DTOs.ReviewListDto;
import org.imdb.clone.DTOs.ReviewUpdateDto;

import org.imdb.clone.Events.MovieRatingRefreshEvent;
import org.imdb.clone.exceptions.ForbiddenOperatorException;
import org.imdb.clone.models.Movie;
import org.imdb.clone.models.Rating;
import org.imdb.clone.models.Review;
import org.imdb.clone.models.User;
import org.imdb.clone.repository.MovieRepository;
import org.imdb.clone.repository.RatingRepository;
import org.imdb.clone.repository.ReviewRepository;
import org.imdb.clone.services.ReviewService;
import org.imdb.clone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.*;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, UserService userService, MovieRepository movieRepository, RatingRepository ratingRepository,
                             ApplicationEventPublisher applicationEventPublisher) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public void postReview(ReviewCreateDto reviewCreateDto) throws Exception {
        User user = userService.getCurrentUser();
        Movie movie = movieRepository.findById(reviewCreateDto.getMovieId()).orElseThrow(() -> new NoSuchElementException("movie not found on id: " + reviewCreateDto.getMovieId()));
        Review review = new Review(reviewCreateDto.getText(), user, movie, reviewCreateDto.getTitle(), reviewCreateDto.isHasSpoiler());
        reviewRepository.save(review);
        if (ratingRepository.findByUserIdAndMovieId(user.getId(), movie.getId()) != null) {
            Rating rating = ratingRepository.findByUserIdAndMovieId(user.getId(), movie.getId());
            rating.setScore(reviewCreateDto.getRating());
            rating.setReview(review);
            ratingRepository.save(rating);
        } else {
            ratingRepository.save(new Rating(reviewCreateDto.getRating(), user, movie, review));
        }

        MovieRatingRefreshEvent movieRatingRefreshEvent = new MovieRatingRefreshEvent(this, movie.getId());
        applicationEventPublisher.publishEvent(movieRatingRefreshEvent);
    }

    @Override
    @Transactional
    public void deleteReviewById(Long id) throws Exception {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Review not found with id: " + id));
        User user = userService.getCurrentUser();

        if (Objects.equals(user.getId(), review.getUser().getId()) || Objects.equals(user.getRole().getValue(), "Moderator")) {
            reviewRepository.delete(review);

            MovieRatingRefreshEvent movieRatingRefreshEvent = new MovieRatingRefreshEvent(this, review.getMovie().getId());
            applicationEventPublisher.publishEvent(movieRatingRefreshEvent);
        } else {
            throw new ForbiddenOperatorException("You don't have permission to delete this review");
        }
    }

    @Override
    public ReviewListDto getReviews(Long userId, Long movieId, boolean hideSpoilers, int pageIndex, int pageCount) {
        PageRequest pageRequest = PageRequest.of(pageIndex, pageCount);
        if (userId != 0 && movieId != 0) {
            Page<ReviewDto> reviews = reviewRepository.findByUserIdAndMovieId(userId, movieId, pageRequest);
            return new ReviewListDto(pageIndex, pageCount, reviews.getTotalElements(), reviews.getContent());
        }
        if (userId == 0) {
            Page<ReviewDto> reviews = reviewRepository.findByMovieId(movieId, pageRequest);
            return new ReviewListDto(pageIndex, pageCount, reviews.getTotalElements(), reviews.getContent());
        }
        if (movieId == 0) {
            Page<ReviewDto> reviews = reviewRepository.findByUserId(userId, pageRequest);
            return new ReviewListDto(pageIndex, pageCount, reviews.getTotalElements(), reviews.getContent());
        }

        return new ReviewListDto(pageIndex, pageCount, 0L, new ArrayList<>());
    }

    @Override
    @Transactional
    public void updateReview(Long id, ReviewUpdateDto reviewUpdateDto) throws Exception {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Review not found with id: " + id));

        User user = userService.getCurrentUser();
        if (!Objects.equals(user.getId(), review.getUser().getId())) {
            throw new ForbiddenOperatorException("You don't have permission to modify this review");
        }

        review.setTitle(reviewUpdateDto.getTitle());
        review.setText(reviewUpdateDto.getText());
        review.setHasSpoiler(reviewUpdateDto.isHasSpoiler());
        reviewRepository.save(review);

        Rating rating = ratingRepository.findByReviewId(review.getId());
        rating.setScore(reviewUpdateDto.getRating());
        ratingRepository.save(rating);

        MovieRatingRefreshEvent movieRatingRefreshEvent = new MovieRatingRefreshEvent(this, rating.getMovie().getId());
        applicationEventPublisher.publishEvent(movieRatingRefreshEvent);
    }


}
