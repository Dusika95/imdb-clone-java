package org.imdb.clone.DTOs;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReviewCreateDto {
    @NotNull(message = "Movie id is required")
    private Long movieId;
    @NotBlank(message = "Text is required")
    private String text;
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull(message = "Has spoiler is required")
    private boolean hasSpoiler;
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "score min value is 1.")
    @Max(value = 5, message = "score max value is 5.")
    private int rating;

    public ReviewCreateDto() {
    }

    public ReviewCreateDto(Long movieId, String text, String title, boolean hasSpoiler, int rating) {
        this.movieId = movieId;
        this.text = text;
        this.title = title;
        this.hasSpoiler = hasSpoiler;
        this.rating = rating;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasSpoiler() {
        return hasSpoiler;
    }

    public void setHasSpoiler(boolean hasSpoiler) {
        this.hasSpoiler = hasSpoiler;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}


