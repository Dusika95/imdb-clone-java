package org.imdb.clone.models;

import javax.persistence.*;

@Entity
@Table(name ="castandcrew")
public class CastAndCrew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "name_id")
    private Name name;
    @Enumerated(EnumType.STRING)
    private MovieRole role;

    public CastAndCrew(){}
    public CastAndCrew( Movie movie, Name name, MovieRole role) {
        this.movie = movie;
        this.name = name;
        this.role = role;
    }
    public CastAndCrew(Long id, Movie movie, Name name, MovieRole role) {
        this.id = id;
        this.movie = movie;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public MovieRole getRole() {
        return role;
    }

    public void setRole(MovieRole role) {
        this.role = role;
    }
}
