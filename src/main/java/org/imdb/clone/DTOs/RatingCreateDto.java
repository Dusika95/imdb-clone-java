package org.imdb.clone.DTOs;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RatingCreateDto {
    @NotNull(message = "Movie id is required")
    private Long movieId;
    @NotNull(message = "Score is required")
    @Min(value = 1, message = "score min value is 1.")
    @Max(value = 5, message = "score max value is 5.")
    private int score;

    public RatingCreateDto() {
    }

    public RatingCreateDto(Long movieId, int score) {
        this.movieId = movieId;
        this.score = score;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
