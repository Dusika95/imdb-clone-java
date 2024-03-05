package org.imdb.clone.DTOs;

public class NameDto {
    private Long id;
    private String fullName;
    public NameDto(){}
    public NameDto(Long id, String fullName){
        this.id=id;
        this.fullName=fullName;
    }
    public String getFullName(){
        return fullName;
    }
    public void setFullName(String fullName){
        this.fullName=fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
