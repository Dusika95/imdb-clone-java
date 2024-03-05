package org.imdb.clone.services;

import org.imdb.clone.DTOs.CurrentScoreDto;
import org.imdb.clone.DTOs.RatingCreateDto;
import org.imdb.clone.DTOs.RatingUpdateDto;

public interface RatingService {
    void postRating(RatingCreateDto ratingCreateDto) throws Exception;

    void deleteRating(Long movieId) throws Exception;

    CurrentScoreDto getRatingByUserIdAndMovieId(Long movieId);

    void updateRating(Long id, RatingUpdateDto ratingUpdateDto) throws Exception;
}
