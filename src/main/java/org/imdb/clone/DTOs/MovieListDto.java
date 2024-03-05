package org.imdb.clone.DTOs;

import java.util.List;

public class MovieListDto {
    private Long total;
    private int pageIndex;
    private List <MovieDto> movieList;

    public MovieListDto(){}

    public MovieListDto(Long total, int pageIndex, List<MovieDto> movieList) {
        this.total = total;
        this.pageIndex = pageIndex;
        this.movieList = movieList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<MovieDto> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<MovieDto> movieList) {
        this.movieList = movieList;
    }


}
