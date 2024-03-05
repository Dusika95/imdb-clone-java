package org.imdb.clone.DTOs;

import org.imdb.clone.models.MovieRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class CastAndCrewCreateDto {
    @NotNull(message = "name id is required")
    private Long nameId;
    @Enumerated(EnumType.STRING)
    private MovieRole movieRole;

    public CastAndCrewCreateDto(){}
    public CastAndCrewCreateDto(Long nameId, MovieRole movieRole) {
        this.nameId = nameId;
        this.movieRole = movieRole;
    }

    public Long getNameId() {
        return nameId;
    }

    public void setNameId(Long nameId) {
        this.nameId = nameId;
    }

    public MovieRole getMovieRole() {
        return movieRole;
    }

    public void setMovieRole(MovieRole movieRole) {
        this.movieRole = movieRole;
    }
}

