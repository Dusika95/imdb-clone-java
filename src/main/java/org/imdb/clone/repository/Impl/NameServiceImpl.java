package org.imdb.clone.repository.Impl;

import org.imdb.clone.DTOs.NameDto;
import org.imdb.clone.DTOs.NameDetailsDto;
import org.imdb.clone.DTOs.NameCreateDto;
import org.imdb.clone.DTOs.MovieInNameDetailsDto;
import org.imdb.clone.models.CastAndCrew;
import org.imdb.clone.models.Movie;
import org.imdb.clone.models.Name;
import org.imdb.clone.repository.CastAndCrewRepository;
import org.imdb.clone.repository.MovieRepository;
import org.imdb.clone.repository.NameRepository;
import org.imdb.clone.services.NameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class NameServiceImpl implements NameService {
    private final NameRepository nameRepository;
    private final CastAndCrewRepository castAndCrewRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public NameServiceImpl(NameRepository nameRepository, CastAndCrewRepository castAndCrewRepository, MovieRepository movieRepository) {
        this.nameRepository = nameRepository;
        this.castAndCrewRepository = castAndCrewRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    @Override
    public void createName(NameCreateDto nameCreateDto) {
        nameRepository.save(new Name(nameCreateDto.getFullName(), nameCreateDto.getDescription()));
    }

    @Override
    public List<NameDto> getAllName() {
        List<Name> nameList = nameRepository.findAll();
        return nameList.stream().map(name -> new NameDto(name.getId(), name.getFullName())).collect(Collectors.toList());
    }

    @Override
    public NameDetailsDto getNameById(Long id) throws Exception {
        Name name = nameRepository.findById(id).orElseThrow(() -> new NoSuchElementException("name not fount on id: " + id));
        List<CastAndCrew> castAndCrewList = castAndCrewRepository.findByNameId(id);

        List<Long> movieIds = castAndCrewList.stream()
                .map(castList -> castList.getMovie().getId())
                .collect(Collectors.toList());

        List<Movie> movies = movieRepository.findAllById(movieIds);

        List<MovieInNameDetailsDto> movieList = new ArrayList<>();

        for (Movie movie : movies) {
            for (CastAndCrew castAndCrew : castAndCrewList) {
                if (castAndCrew.getMovie().getId().equals(movie.getId())) {
                    movieList.add(new MovieInNameDetailsDto(movie.getId(), movie.getTitle(), castAndCrew.getRole().getValue()));
                    break;
                }
            }
        }
        return new NameDetailsDto(name.getId(), name.getFullName(), name.getDescription(), movieList);
    }


    @Override
    @Transactional
    public void updateName(Long id, NameCreateDto nameCreateDto) throws Exception {
        Name name = nameRepository.findById(id).orElseThrow(() -> new NoSuchElementException("name not fount on id: " + id));
        name.setFullName(nameCreateDto.getFullName());
        name.setDescription(nameCreateDto.getDescription());
        nameRepository.save(name);
    }
}
