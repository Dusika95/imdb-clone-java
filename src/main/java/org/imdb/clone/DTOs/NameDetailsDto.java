package org.imdb.clone.DTOs;

import java.util.List;

public class NameDetailsDto {
    private Long id;
    private String fullName;
    private String description;
    private List<MovieInNameDetailsDto> movieList;
    public NameDetailsDto(){}

    public NameDetailsDto(Long id, String fullName, String description, List<MovieInNameDetailsDto> moiveList) {
        this.id = id;
        this.fullName = fullName;
        this.description = description;
        this.movieList = moiveList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<MovieInNameDetailsDto> getMoiveList() {
        return movieList;
    }

    public void setMoiveList(List<MovieInNameDetailsDto> moiveList) {
        this.movieList = moiveList;
    }
}

