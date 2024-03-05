package org.imdb.clone.DTOs;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RatingUpdateDto {
    @NotNull(message = "Score is required")
    @Min(value = 1, message = "score min value is 1.")
    @Max(value = 5, message = "score max value is 5.")
    private int score;

    public RatingUpdateDto(){}
    public RatingUpdateDto(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
