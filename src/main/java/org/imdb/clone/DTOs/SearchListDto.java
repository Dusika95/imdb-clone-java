package org.imdb.clone.DTOs;

import java.util.List;

public class SearchListDto {
    private Long totalNames;
    private Long totalMovies;
    private int namePageIndex;
    private int moviePageIndex;
    private List<MovieInSearchDto> movieList;
    private List<NameInSearchDto> nameList;

    public SearchListDto() {
    }

    public SearchListDto(Long totalNames, Long totalMovies, int namePageIndex, int moviePageIndex, List<MovieInSearchDto> movieList, List<NameInSearchDto> nameList) {
        this.totalNames = totalNames;
        this.totalMovies = totalMovies;
        this.namePageIndex = namePageIndex;
        this.moviePageIndex = moviePageIndex;
        this.movieList = movieList;
        this.nameList = nameList;
    }

    public Long getTotalNames() {
        return totalNames;
    }

    public void setTotalNames(Long totalNames) {
        this.totalNames = totalNames;
    }

    public Long getTotalMovies() {
        return totalMovies;
    }

    public void setTotalMovies(Long totalMovies) {
        this.totalMovies = totalMovies;
    }

    public int getNamePageIndex() {
        return namePageIndex;
    }

    public void setNamePageIndex(int namePageIndex) {
        this.namePageIndex = namePageIndex;
    }

    public int getMoviePageIndex() {
        return moviePageIndex;
    }

    public void setMoviePageIndex(int moviePageIndex) {
        this.moviePageIndex = moviePageIndex;
    }

    public List<MovieInSearchDto> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<MovieInSearchDto> movieList) {
        this.movieList = movieList;
    }

    public List<NameInSearchDto> getNameList() {
        return nameList;
    }

    public void setNameList(List<NameInSearchDto> nameList) {
        this.nameList = nameList;
    }
}
