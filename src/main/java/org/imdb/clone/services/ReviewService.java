package org.imdb.clone.services;


import org.imdb.clone.DTOs.ReviewCreateDto;
import org.imdb.clone.DTOs.ReviewListDto;
import org.imdb.clone.DTOs.ReviewUpdateDto;

public interface ReviewService {
    void postReview(ReviewCreateDto reviewCreateDto) throws Exception;

    void deleteReviewById(Long id) throws Exception;

    ReviewListDto getReviews(Long userId, Long movieId, boolean hideSpoilers, int pageIndex, int pageCount);

    void updateReview(Long id, ReviewUpdateDto reviewUpdateDto) throws Exception;
}
