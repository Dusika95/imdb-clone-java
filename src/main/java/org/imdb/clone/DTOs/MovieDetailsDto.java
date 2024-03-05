package org.imdb.clone.DTOs;

import java.util.Date;
import java.util.List;

public class MovieDetailsDto {

    private Long id;
    private String title;
    private double rating;
    private String description;
    private Date releaseDate;
    private List<MemberDto> castAndCrew;

    public MovieDetailsDto(){}
    public MovieDetailsDto(Long id, String title, double rating, String description, Date releaseDate, List<MemberDto> castAndCrew) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.description = description;
        this.releaseDate = releaseDate;
        this.castAndCrew = castAndCrew;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<MemberDto> getCastAndCrew() {
        return castAndCrew;
    }

    public void setCastAndCrew(List<MemberDto> castAndCrew) {
        this.castAndCrew = castAndCrew;
    }
}
