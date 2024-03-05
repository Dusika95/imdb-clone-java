package org.imdb.clone.DTOs;

import java.util.Date;

public class MovieInSearchDto {
    private Long id;
    private String title;
    private Date releaseDate;

    public MovieInSearchDto() {
    }

    public MovieInSearchDto(Long id, String title, Date releaseDate) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
