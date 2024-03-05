package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.CurrentScoreDto;
import org.imdb.clone.DTOs.RatingCreateDto;
import org.imdb.clone.DTOs.RatingUpdateDto;
import org.imdb.clone.DTOs.UserLoginDto;
import org.imdb.clone.models.Movie;
import org.imdb.clone.models.Rating;
import org.imdb.clone.models.User;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;


import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RatingControllerTest {
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost:";
    @Autowired
    TestH2UserRepository userRepository;
    @Autowired
    TestH2MovieRepository movieRepository;
    @Autowired
    TestH2RatingRepository ratingRepository;
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
    void postRating() throws Exception {
        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2020 - 1 - 1), "fantasztikus, csodálatos film");
        movieRepository.save(movie);

        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);
        UserLoginDto userLoginDTO = new UserLoginDto("user@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        RatingCreateDto ratingCreateDto = new RatingCreateDto(movie.getId(), 3);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<RatingCreateDto> request = new HttpEntity<>(ratingCreateDto, headers);

        ResponseEntity<RatingCreateDto> response = restTemplate.postForEntity(baseUrl + port + "/ratings", request, RatingCreateDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ratingRepository.findAll().get(0).getScore(), ratingCreateDto.getScore());
        assertEquals(ratingRepository.findAll().get(0).getMovie().getRating(), ratingCreateDto.getScore());
        assertEquals(ratingRepository.findAll().get(0).getUser().getId(), user.getId());
        assertEquals(ratingRepository.findAll().get(0).getMovie().getId(), movie.getId());

    }

    @Test
    void deleteRating() throws Exception {
        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2020 - 1 - 1), "fantasztikus, csodálatos film");
        movieRepository.save(movie);

        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        Rating rating = new Rating(3, user, movie);
        ratingRepository.save(rating);

        User moderator = new User("moderator1", "moderator@email.com", Role.valueOf("Moderator"), passwordEncoder.encode("idontcare"));
        userRepository.save(moderator);

        UserLoginDto userLoginDTO = new UserLoginDto("moderator@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/ratings/" + rating.getId(), HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, ratingRepository.findAll().size());

    }

    @Test
    void getRatingByUserIdAndMovieId() throws Exception {
        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2020 - 1 - 1), "fantasztikus, csodálatos film");
        movieRepository.save(movie);

        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        Rating rating = new Rating(3, user, movie);
        ratingRepository.save(rating);

        UserLoginDto userLoginDTO = new UserLoginDto("user@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<CurrentScoreDto> response = restTemplate.exchange(baseUrl + port + "/ratings?movieId=" + movie.getId(), HttpMethod.GET, new HttpEntity<>(headers), CurrentScoreDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getCurrentScore(), rating.getScore());
    }

    @Test
    void updateRating() throws Exception {
        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2020 - 1 - 1), "fantasztikus, csodálatos film");
        movieRepository.save(movie);

        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        Rating rating = new Rating(3, user, movie);
        ratingRepository.save(rating);

        UserLoginDto userLoginDTO = new UserLoginDto("user@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        RatingUpdateDto ratingUpdateDto = new RatingUpdateDto(4);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<RatingUpdateDto> request = new HttpEntity<>(ratingUpdateDto, headers);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/ratings/" + rating.getId(), HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ratingRepository.findAll().get(0).getScore(), ratingUpdateDto.getScore());
    }
}