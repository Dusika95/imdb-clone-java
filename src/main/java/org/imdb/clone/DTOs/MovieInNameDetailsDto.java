package org.imdb.clone.DTOs;

public class MovieInNameDetailsDto {
    private Long movieId;
    private String movieTitle;
    private String role;

    public MovieInNameDetailsDto(){}
    public MovieInNameDetailsDto(Long movieId, String movieTitle, String role) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.role = role;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

