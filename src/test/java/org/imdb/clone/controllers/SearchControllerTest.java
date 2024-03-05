package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.SearchListDto;
import org.imdb.clone.models.Movie;
import org.imdb.clone.models.Name;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SearchControllerTest {
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost:";
    @Autowired
    TestH2MovieRepository movieRepository;
    @Autowired
    TestH2NameRepository nameRepository;
    private static RestTemplate restTemplate;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @Test
    void search() {
        Movie movie = new Movie("A vakond asszony visszanéz", 3, new Date(2000 - 1 - 3), "a vakond általánosságban vak, de ez egy nem hétköznapi story");
        movieRepository.save(movie);

        Name name = new Name("Vakegér Jóska Pista", "fapofa");
        nameRepository.save(name);

        ResponseEntity<SearchListDto> response = restTemplate.getForEntity(baseUrl + port + "/search?searchType=all&searchText=vAk", SearchListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).getMoviePageIndex());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getTotalMovies());
        assertEquals(movie.getTitle(), Objects.requireNonNull(response.getBody()).getMovieList().get(0).getTitle());
        assertEquals(movie.getId(), Objects.requireNonNull(response.getBody()).getMovieList().get(0).getId());
        assertEquals(movie.getReleaseDate(), Objects.requireNonNull(response.getBody()).getMovieList().get(0).getReleaseDate());

        assertEquals(0, Objects.requireNonNull(response.getBody()).getNamePageIndex());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getTotalNames());
        assertEquals(name.getFullName(), Objects.requireNonNull(response.getBody()).getNameList().get(0).getFullName());
        assertEquals(name.getId(), Objects.requireNonNull(response.getBody()).getNameList().get(0).getId());

    }

    @Test
    void searchWithSearchTypeName() {
        Movie movie = new Movie("A vakond asszony visszanéz", 3, new Date(2000 - 1 - 3), "a vakond általánosságban vak, de ez egy nem hétköznapi story");
        movieRepository.save(movie);

        Name name = new Name("Vakegér Jóska Pista", "fapofa");
        nameRepository.save(name);

        ResponseEntity<SearchListDto> response = restTemplate.getForEntity(baseUrl + port + "/search?searchType=name&searchText=vAk", SearchListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getMovieList());
        assertNull(response.getBody().getTotalMovies());

        assertEquals(0, Objects.requireNonNull(response.getBody()).getNamePageIndex());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getTotalNames());
        assertEquals(name.getFullName(), Objects.requireNonNull(response.getBody()).getNameList().get(0).getFullName());
        assertEquals(name.getId(), Objects.requireNonNull(response.getBody()).getNameList().get(0).getId());
    }

    @Test
    void searchWithSearchTypeAllNoMovieResult() {
        Movie movie = new Movie("A vakond asszony visszanézz", 3, new Date(2000 - 1 - 3), "a vakond általánosságban vak, de ez egy nem hétköznapi story");
        movieRepository.save(movie);

        Name name = new Name("Vakegér Jóska Pista", "fapofa");
        nameRepository.save(name);

        ResponseEntity<SearchListDto> response = restTemplate.getForEntity(baseUrl + port + "/search?searchType=name&searchText=pista", SearchListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNull(response.getBody().getMovieList());
        assertNull(response.getBody().getTotalMovies());

        assertEquals(0, Objects.requireNonNull(response.getBody()).getNamePageIndex());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getTotalNames());
        assertEquals(name.getFullName(), Objects.requireNonNull(response.getBody()).getNameList().get(0).getFullName());
        assertEquals(name.getId(), Objects.requireNonNull(response.getBody()).getNameList().get(0).getId());
    }

    @Test
    void searchTestPaging() {
        List<String> lastNames = new ArrayList<>(List.of("Vak Jóska", "Erős", "Pataki", "Nagy", "Kis", "Bajszos"));
        List<Name> nameList = new ArrayList<>();

        for (String lastName : lastNames) {
            nameList.add(new Name(lastName + " Pista", "fapofa"));
        }

        nameRepository.saveAll(nameList);

        ResponseEntity<SearchListDto> response = restTemplate.getForEntity(baseUrl + port + "/search?searchType=all&searchText=pista&namePageIndex=1", SearchListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getNamePageIndex());
        assertEquals(6, response.getBody().getTotalNames());
        assertEquals(nameList.get(5).getFullName(), Objects.requireNonNull(response.getBody()).getNameList().get(0).getFullName());
        assertEquals(nameList.get(5).getId(), Objects.requireNonNull(response.getBody()).getNameList().get(0).getId());
    }
}