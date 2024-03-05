package org.imdb.clone.repository.Impl;

import org.imdb.clone.DTOs.CastAndCrewWithNameDto;
import org.imdb.clone.DTOs.MovieDetailsDto;
import org.imdb.clone.DTOs.MovieListDto;
import org.imdb.clone.DTOs.MovieCreateDto;
import org.imdb.clone.DTOs.MemberDto;
import org.imdb.clone.DTOs.MovieDto;
import org.imdb.clone.DTOs.CastAndCrewCreateDto;
import org.imdb.clone.models.CastAndCrew;
import org.imdb.clone.models.Movie;
import org.imdb.clone.models.Name;
import org.imdb.clone.repository.CastAndCrewRepository;
import org.imdb.clone.repository.MovieRepository;
import org.imdb.clone.repository.NameRepository;
import org.imdb.clone.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final NameRepository nameRepository;
    private final CastAndCrewRepository castAndCrewRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, NameRepository nameRepository, CastAndCrewRepository castAndCrewRepository) {
        this.movieRepository = movieRepository;
        this.nameRepository = nameRepository;
        this.castAndCrewRepository = castAndCrewRepository;
    }

    @Override
    public MovieListDto getAllMovies(int pageIndex) {
        int pageSize = 30;
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);
        Page<Movie> moviePage = movieRepository.findAll(pageRequest);

        MovieListDto movieListDTO = new MovieListDto();
        movieListDTO.setTotal(moviePage.getTotalElements());
        movieListDTO.setPageIndex(pageIndex);

        List<MovieDto> movieDtoList = moviePage.stream()
                .map(movie -> new MovieDto(movie.getId(), movie.getTitle(), movie.getRating(), movie.getReleaseDate()))
                .collect(Collectors.toList());

        movieListDTO.setMovieList(movieDtoList);

        return movieListDTO;
    }

    @Override
    public MovieDetailsDto getMovieById(Long id) throws Exception {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new NoSuchElementException("movie not found on id: " + id));

        List<CastAndCrewWithNameDto> CastAndCrewWithNameDtoList = castAndCrewRepository.findCrewWithNamesByMovieId(movie.getId());
        List<MemberDto> memberDtoList = CastAndCrewWithNameDtoList.stream()
                .map(list -> new MemberDto(list.getId(), list.getFullName(), list.getRole().getValue()))
                .collect(Collectors.toList());

        return new MovieDetailsDto(movie.getId(), movie.getTitle(), movie.getRating(), movie.getDescription(), movie.getReleaseDate(), memberDtoList);
    }

    @Override
    @Transactional
    public void createMovie(MovieCreateDto movieCreateDto) throws Exception {
        Movie movie = new Movie(movieCreateDto.getTitle(), 0, movieCreateDto.getReleaseDate(), movieCreateDto.getDescription());
        movieRepository.save(movie);

        List<Long> nameIds = movieCreateDto.getCastAndCrew().stream()
                .map(CastAndCrewCreateDto::getNameId).collect(Collectors.toList());

        List<Name> nameList = nameRepository.findAllById(nameIds);

        Map<Long, Name> nameMap = nameList.stream()
                .collect(Collectors.toMap(Name::getId, Function.identity()));

        List<CastAndCrew> castAndCrewList = new ArrayList<>();

        for (CastAndCrewCreateDto castAndCrewCreateDto : movieCreateDto.getCastAndCrew()) {
            Long nameId = castAndCrewCreateDto.getNameId();
            Name name = nameMap.get(nameId);
            if (name == null) {
                throw new NoSuchElementException("Name not found with id: " + nameId);
            }

            castAndCrewList.add(new CastAndCrew(movie, name, castAndCrewCreateDto.getMovieRole()));

        }
        castAndCrewRepository.saveAll(castAndCrewList);
    }

    @Override
    @Transactional
    public void updateMovie(MovieCreateDto movieCreateDto, Long id) throws Exception {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new NoSuchElementException("movie not found with id: " + id));
        movie.setDescription(movieCreateDto.getDescription());
        movie.setTitle(movieCreateDto.getTitle());
        movie.setReleaseDate(movieCreateDto.getReleaseDate());
        movieRepository.save(movie);

        castAndCrewRepository.deleteByMovieId(id);

        List<Long> nameIds = movieCreateDto.getCastAndCrew().stream()
                .map(CastAndCrewCreateDto::getNameId).collect(Collectors.toList());

        List<Name> nameList = nameRepository.findAllById(nameIds);

        Map<Long, Name> nameMap = nameList.stream()
                .collect(Collectors.toMap(Name::getId, Function.identity()));

        List<CastAndCrew> castAndCrewList = new ArrayList<>();

        for (CastAndCrewCreateDto castAndCrewCreateDto : movieCreateDto.getCastAndCrew()) {
            Long nameId = castAndCrewCreateDto.getNameId();
            Name name = nameMap.get(nameId);
            if (name == null) {
                throw new NoSuchElementException("Name not found for id: " + nameId);
            }

            castAndCrewList.add(new CastAndCrew(movie, name, castAndCrewCreateDto.getMovieRole()));
        }

        List<CastAndCrew> savedCastAndCrewList = castAndCrewRepository.saveAll(castAndCrewList);
    }
}
