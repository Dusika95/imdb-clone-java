package org.imdb.clone.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    private String title;
    private boolean hasSpoiler;

    @OneToMany(mappedBy = "review",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    public Review() {

    }

    public Review( String text, User user, Movie movie, String title, boolean hasSpoiler) {
        this.text = text;
        this.user = user;
        this.movie = movie;
        this.title = title;
        this.hasSpoiler = hasSpoiler;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasSpoiler() {
        return hasSpoiler;
    }

    public void setHasSpoiler(boolean hasSpoiler) {
        this.hasSpoiler = hasSpoiler;
    }
}
