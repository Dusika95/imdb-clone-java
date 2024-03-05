package org.imdb.clone.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class MovieCreateDto {
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull(message = "Release date is required")
    private Date releaseDate;
    @NotBlank(message = "Description is required")
    private String description;
    @NotEmpty(message = "Cast and crew list is required")
    private List<CastAndCrewCreateDto> castAndCrew;

    public MovieCreateDto() {
    }

    public MovieCreateDto(String title, Date releaseDate, String description, List<CastAndCrewCreateDto> castAndCrew) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.description = description;
        this.castAndCrew = castAndCrew;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CastAndCrewCreateDto> getCastAndCrew() {
        return castAndCrew;
    }

    public void setCastAndCrew(List<CastAndCrewCreateDto> castAndCrew) {
        this.castAndCrew = castAndCrew;
    }
}
