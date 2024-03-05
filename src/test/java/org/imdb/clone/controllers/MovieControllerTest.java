package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.MovieDetailsDto;
import org.imdb.clone.DTOs.MovieListDto;
import org.imdb.clone.DTOs.MovieCreateDto;
import org.imdb.clone.DTOs.UserLoginDto;
import org.imdb.clone.DTOs.CastAndCrewCreateDto;
import org.imdb.clone.models.CastAndCrew;
import org.imdb.clone.models.Movie;
import org.imdb.clone.models.Name;
import org.imdb.clone.models.User;
import org.imdb.clone.models.MovieRole;
import org.imdb.clone.models.Role;
import org.imdb.clone.repository.Impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MovieControllerTest {
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost:";
    @Autowired
    TestH2MovieRepository movieRepository;
    @Autowired
    TestH2NameRepository nameRepository;
    @Autowired
    TestH2UserRepository userRepository;
    @Autowired
    TestH2CastAndCrewRepository castAndCrewRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    AuthServiceImpl authService;
    private static RestTemplate restTemplate;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @Test
    void TestGetAllMovies() {
        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2000 - 1 - 3), "fantasztikus, csodálatos film");
        movieRepository.save(movie);

        ResponseEntity<MovieListDto> response = restTemplate.getForEntity(baseUrl + port + "/movies", MovieListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getTotal());
        assertEquals(0, response.getBody().getPageIndex());
        assertEquals(1, response.getBody().getMovieList().size());
        assertEquals(movie.getId(), response.getBody().getMovieList().get(0).getId());
        assertEquals(movie.getTitle(), response.getBody().getMovieList().get(0).getTitle());
        assertEquals(movie.getRating(), response.getBody().getMovieList().get(0).getRating());
        assertEquals(movie.getReleaseDate(), response.getBody().getMovieList().get(0).getReleaseDate());
    }

    @Test
    void TestGetAllMoviesWithEmptyTable() {

        ResponseEntity<MovieListDto> response = restTemplate.getForEntity(baseUrl + port + "/movies", MovieListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).getTotal());
        assertEquals(0, response.getBody().getPageIndex());
        assertEquals(0, response.getBody().getMovieList().size());
    }

    @Test
    void TestGetAllMoviesPages() {
        List<Movie> movieList = new ArrayList<>();
        int serialNumber = 1;
        for (int i = 0; i < 31; i++) {
            movieList.add(new Movie("A vak asszony visszanéz " + serialNumber, 0, new Date(200 + serialNumber - 1 - 3), "fantasztikus, csodálatos film"));
        }
        movieRepository.saveAll(movieList);

        ResponseEntity<MovieListDto> response = restTemplate.getForEntity(baseUrl + port + "/movies?pageIndex=1", MovieListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(31, Objects.requireNonNull(response.getBody()).getTotal());
        assertEquals(1, response.getBody().getPageIndex());
        assertEquals(1, response.getBody().getMovieList().size());
        assertEquals(movieList.get(30).getId(), response.getBody().getMovieList().get(0).getId());
        assertEquals(movieList.get(30).getTitle(), response.getBody().getMovieList().get(0).getTitle());
        assertEquals(movieList.get(30).getRating(), response.getBody().getMovieList().get(0).getRating());
        assertEquals(movieList.get(30).getReleaseDate(), response.getBody().getMovieList().get(0).getReleaseDate());
    }

    @Test
    void getMovieDetailsById() {
        Movie movie = new Movie("A vak asszony visszanézz", 3, new Date(2000 - 1 - 3), "fantasztikus, csodálatos film");
        movieRepository.save(movie);

        Name name = new Name("Jóska Pista", "fapofa");
        nameRepository.save(name);

        CastAndCrew castAndCrew = new CastAndCrew(movie, name, MovieRole.valueOf("Actor"));
        castAndCrewRepository.save(castAndCrew);

        ResponseEntity<MovieDetailsDto> response = restTemplate.getForEntity(baseUrl + port + "/movies/" + movie.getId(), MovieDetailsDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movie.getId(), response.getBody().getId());
        assertEquals(movie.getTitle(), response.getBody().getTitle());
        assertEquals(movie.getRating(), response.getBody().getRating());
        assertEquals(movie.getDescription(), response.getBody().getDescription());
        assertEquals(movie.getReleaseDate(), Objects.requireNonNull(response.getBody()).getReleaseDate());
        assertEquals("Jóska Pista", response.getBody().getCastAndCrew().get(0).getFullName());
        assertEquals("Actor", response.getBody().getCastAndCrew().get(0).getRole());
        assertEquals(name.getId(), response.getBody().getCastAndCrew().get(0).getId());
    }

    @Test
    void postMovie() throws Exception {
        User editor = new User("editor", "editor@email.com", Role.valueOf("Editor"), passwordEncoder.encode("idontcare"));
        userRepository.save(editor);
        UserLoginDto userLoginDTO = new UserLoginDto("editor@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        Name name = new Name("Jóska Pista", "fapofa");
        nameRepository.save(name);

        CastAndCrewCreateDto castAndCrewCreateDto = new CastAndCrewCreateDto(name.getId(), MovieRole.valueOf("Actor"));
        List<CastAndCrewCreateDto> castAndCrewList = new ArrayList<>();
        castAndCrewList.add(castAndCrewCreateDto);

        MovieCreateDto movieCreateDto = new MovieCreateDto("A vak asszony visszanéz", new Date(2020 - 1 - 1), "szuper filmecske", castAndCrewList);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<MovieCreateDto> request = new HttpEntity<>(movieCreateDto, headers);

        ResponseEntity<MovieCreateDto> response = restTemplate.postForEntity(baseUrl + port + "/movies", request, MovieCreateDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, movieRepository.findAll().get(0).getId());
        assertEquals(movieCreateDto.getTitle(), movieRepository.findAll().get(0).getTitle());
        assertEquals(movieCreateDto.getDescription(), movieRepository.findAll().get(0).getDescription());
        assertEquals(movieCreateDto.getReleaseDate(), movieRepository.findAll().get(0).getReleaseDate());
        assertEquals("Actor", castAndCrewRepository.findAll().get(0).getRole().getValue());
        assertEquals(1, castAndCrewRepository.findAll().get(0).getMovie().getId());
        assertEquals(name.getId(), castAndCrewRepository.findAll().get(0).getName().getId());
    }


    @Test
    void updateMovie() throws Exception {
        User editor = new User("editor", "editor@email.com", Role.valueOf("Editor"), passwordEncoder.encode("idontcare"));
        userRepository.save(editor);

        Movie movie = new Movie("A vak asszony viszanéz typo-val", 0, new Date(2000 - 1 - 3), "fantasztikus, csodlatos film typo-val");
        movieRepository.save(movie);

        Name name = new Name("Jóska Pista", "fapofa");
        nameRepository.save(name);

        CastAndCrew castAndCrew = new CastAndCrew(movie, name, MovieRole.valueOf("Actor"));
        castAndCrewRepository.save(castAndCrew);

        Name nameToChanges = new Name("Ifj Jóska Pista", "kevésbbé fapofa mint az apja");
        nameRepository.save(nameToChanges);

        CastAndCrewCreateDto castAndCrewCreateDto = new CastAndCrewCreateDto(nameToChanges.getId(), MovieRole.valueOf("Actor"));
        List<CastAndCrewCreateDto> castAndCrewList = new ArrayList<>(List.of(castAndCrewCreateDto));

        MovieCreateDto movieCreateDto = new MovieCreateDto("Vak asszony visszanéz", new Date(2020 - 2 - 1), "fantasztikus, csodálatos film", castAndCrewList);

        UserLoginDto userLoginDTO = new UserLoginDto("editor@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<MovieCreateDto> request = new HttpEntity<>(movieCreateDto, headers);

        System.out.println("ez az id: " + movie.getId());
        System.out.println(movieRepository.findAll().get(0).getTitle() + " " + movieRepository.findAll().get(0).getId());

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/movies/" + movie.getId(), HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movie.getId(), movieRepository.findAll().get(0).getId());
        assertEquals(movieCreateDto.getTitle(), movieRepository.findAll().get(0).getTitle());
    }

}