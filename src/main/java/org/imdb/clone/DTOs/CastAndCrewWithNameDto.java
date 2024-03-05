package org.imdb.clone.DTOs;

import org.imdb.clone.models.MovieRole;

public class CastAndCrewWithNameDto {
    private Long nameId;
    private MovieRole role;
    private String fullName;
    private String description;

    public CastAndCrewWithNameDto(){}
    public CastAndCrewWithNameDto(Long nameId, MovieRole role, String fullName, String description) {
        this.nameId = nameId;
        this.role = role;
        this.fullName = fullName;
        this.description = description;
    }

    public Long getId() {
        return nameId;
    }

    public void setId(Long nameId) {
        this.nameId = nameId;
    }

    public MovieRole getRole() {
        return role;
    }

    public void setRole(MovieRole role) {
        this.role = role;
    }



    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
