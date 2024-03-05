package org.imdb.clone.DTOs;

public class ReviewDto {
    private Long id;
    private Long movieId;
    private String creatorName;
    private String movieTitle;
    private String text;
    private String reviewTitle;
    private boolean hasSpoiler;
    private int rating;

    public ReviewDto(){}
    public ReviewDto(Long id, Long movieId, String creatorName, String movieTitle, String text, String reviewTitle, boolean hasSpoiler, int rating) {
        this.id = id;
        this.movieId = movieId;
        this.creatorName = creatorName;
        this.movieTitle = movieTitle;
        this.text = text;
        this.reviewTitle = reviewTitle;
        this.hasSpoiler = hasSpoiler;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
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