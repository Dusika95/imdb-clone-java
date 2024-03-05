package org.imdb.clone.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "names")
public class Name {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String description;
    @OneToMany(mappedBy = "name",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CastAndCrew> castAndCrew = new ArrayList<>();

    public Name(){}

    public Name(String fullName, String description){
        this.fullName=fullName;
        this.description=description;
    }
    public Name(Long id, String fullName, String description) {
        this.id = id;
        this.fullName = fullName;
        this.description = description;
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
}
