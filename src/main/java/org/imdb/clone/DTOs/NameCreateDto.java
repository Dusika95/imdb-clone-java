package org.imdb.clone.DTOs;


import javax.validation.constraints.NotBlank;

public class NameCreateDto {
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotBlank(message = "Description is required")
    private String description;

    public NameCreateDto(){}
    public NameCreateDto(String fullName, String description) {
        this.fullName = fullName;
        this.description = description;
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
