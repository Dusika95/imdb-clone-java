package org.imdb.clone.Events;

import org.springframework.context.ApplicationEvent;

public class MovieRatingRefreshEvent extends ApplicationEvent {
    private final Long movieId;

    public MovieRatingRefreshEvent(Object source, Long movieId) {
        super(source);
        this.movieId = movieId;
    }

    public Long getMovieId() {
        return movieId;
    }
}
