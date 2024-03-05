package org.imdb.clone.repository.Impl;

import org.imdb.clone.DTOs.SearchListDto;
import org.imdb.clone.DTOs.MovieInSearchDto;
import org.imdb.clone.DTOs.NameInSearchDto;
import org.imdb.clone.models.Movie;
import org.imdb.clone.models.Name;
import org.imdb.clone.repository.MovieRepository;
import org.imdb.clone.repository.NameRepository;
import org.imdb.clone.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SearchServiceImpl implements SearchService {
    private final MovieRepository movieRepository;
    private final NameRepository nameRepository;

    @Autowired
    public SearchServiceImpl(MovieRepository movieRepository, NameRepository nameRepository) {
        this.movieRepository = movieRepository;
        this.nameRepository = nameRepository;
    }

    @Override
    public SearchListDto search(String searchText, String searchType, int moviePageIndex, int namePageIndex) {
        int pageSize = 5;
        SearchListDto searchListDTO = new SearchListDto();
        if (Objects.equals(searchType, "movie") || Objects.equals(searchType, "all")) {
            PageRequest moviePageRequest = PageRequest.of(moviePageIndex, pageSize);
            Page<Movie> movieList = movieRepository.searchMoviesByTitle(searchText, moviePageRequest);
            List<MovieInSearchDto> movies = new ArrayList<>();
            for (Movie movie : movieList) {
                MovieInSearchDto movieInSearchDTO = new MovieInSearchDto(movie.getId(), movie.getTitle(), movie.getReleaseDate());
                movies.add(movieInSearchDTO);
            }
            searchListDTO.setMovieList(movies);
            searchListDTO.setMoviePageIndex(moviePageIndex);
            searchListDTO.setTotalMovies(movieList.getTotalElements());
        }
        if (Objects.equals(searchType, "name") || Objects.equals(searchType, "all")) {
            PageRequest namePageRequest = PageRequest.of(namePageIndex, pageSize);
            Page<Name> nameList = nameRepository.searchNamesByFullName(searchText, namePageRequest);
            List<NameInSearchDto> names = new ArrayList<>();
            for (Name name : nameList) {
                NameInSearchDto nameInSearchDTO = new NameInSearchDto(name.getId(), name.getFullName());
                names.add(nameInSearchDTO);
            }
            searchListDTO.setNameList(names);
            searchListDTO.setNamePageIndex(namePageIndex);
            searchListDTO.setTotalNames(nameList.getTotalElements());
        }

        return searchListDTO;
    }
}
