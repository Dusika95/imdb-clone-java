package org.imdb.clone.services;

import org.imdb.clone.DTOs.MovieDetailsDto;
import org.imdb.clone.DTOs.MovieListDto;
import org.imdb.clone.DTOs.MovieCreateDto;

public interface MovieService {
    MovieListDto getAllMovies(int pageIndex);
    MovieDetailsDto getMovieById(Long id) throws Exception;
    void createMovie(MovieCreateDto movieCreateDto) throws Exception;
    void updateMovie(MovieCreateDto movieCreateDto, Long id)throws Exception;
}
