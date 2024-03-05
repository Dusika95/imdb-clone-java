package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.ReviewCreateDto;
import org.imdb.clone.DTOs.ReviewListDto;
import org.imdb.clone.DTOs.ReviewUpdateDto;
import org.imdb.clone.DTOs.UserLoginDto;
import org.imdb.clone.models.Movie;
import org.imdb.clone.models.Rating;
import org.imdb.clone.models.Review;
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
class ReviewControllerTest {
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost:";
    @Autowired
    TestH2UserRepository userRepository;
    @Autowired
    TestH2MovieRepository movieRepository;
    @Autowired
    TestH2ReviewRepository reviewRepository;
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
    void postReview() throws Exception {
        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2020 - 1 - 1), "egy megvakult nő képes nézni, nem is akárhogyan, képes visszanézni!");
        movieRepository.save(movie);

        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(movie.getId(), "nekem teCCet!4", "nice", false, 4);

        UserLoginDto userLoginDTO = new UserLoginDto("user@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<ReviewCreateDto> request = new HttpEntity<>(reviewCreateDto, headers);

        ResponseEntity<ReviewCreateDto> response = restTemplate.postForEntity(baseUrl + port + "/reviews", request, ReviewCreateDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, reviewRepository.findAll().size());
        assertEquals(1, reviewRepository.findAll().get(0).getId());
        assertEquals(reviewCreateDto.getMovieId(), reviewRepository.findAll().get(0).getMovie().getId());
        assertEquals(user.getId(), reviewRepository.findAll().get(0).getUser().getId());
        assertEquals(reviewCreateDto.getTitle(), reviewRepository.findAll().get(0).getTitle());
        assertEquals(reviewCreateDto.getText(), reviewRepository.findAll().get(0).getText());

        assertEquals(1, ratingRepository.findAll().size());
        assertEquals(reviewCreateDto.getRating(), ratingRepository.findAll().get(0).getScore());
        assertEquals(reviewCreateDto.getMovieId(), ratingRepository.findAll().get(0).getMovie().getId());
        assertEquals(user.getId(), ratingRepository.findAll().get(0).getUser().getId());
        assertEquals(reviewRepository.findAll().get(0).getId(), ratingRepository.findAll().get(0).getReview().getId());

        assertEquals(reviewCreateDto.getRating(), movieRepository.findAll().get(0).getRating());
    }

    @Test
    void postReviewWithExistingRating() throws Exception {
        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2020 - 1 - 1), "egy megvakult nő képes nézni, nem is akárhogyan, képes visszanézni!");
        movieRepository.save(movie);

        Rating rating = new Rating(2, user, movie);
        ratingRepository.save(rating);

        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(movie.getId(), "nekem teCCet!4", "nice", false, 4);

        UserLoginDto userLoginDTO = new UserLoginDto("user@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<ReviewCreateDto> request = new HttpEntity<>(reviewCreateDto, headers);

        ResponseEntity<ReviewCreateDto> response = restTemplate.postForEntity(baseUrl + port + "/reviews", request, ReviewCreateDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, reviewRepository.findAll().size());
        assertEquals(1, reviewRepository.findAll().get(0).getId());
        assertEquals(reviewCreateDto.getMovieId(), reviewRepository.findAll().get(0).getMovie().getId());
        assertEquals(user.getId(), reviewRepository.findAll().get(0).getUser().getId());
        assertEquals(reviewCreateDto.getTitle(), reviewRepository.findAll().get(0).getTitle());
        assertEquals(reviewCreateDto.getText(), reviewRepository.findAll().get(0).getText());

        assertEquals(1, ratingRepository.findAll().size());
        assertEquals(reviewCreateDto.getRating(), ratingRepository.findAll().get(0).getScore());
        assertEquals(reviewCreateDto.getMovieId(), ratingRepository.findAll().get(0).getMovie().getId());
        assertEquals(user.getId(), ratingRepository.findAll().get(0).getUser().getId());
        assertEquals(reviewRepository.findAll().get(0).getId(), ratingRepository.findAll().get(0).getReview().getId());

        assertEquals(reviewCreateDto.getRating(), movieRepository.findAll().get(0).getRating());
    }

    @Test
    void deleteReviewByIdByCreator() throws Exception {
        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2020 - 1 - 1), "egy megvakult nő képes nézni, nem is akárhogyan, képes visszanézni!");
        movieRepository.save(movie);

        Review review = new Review("nekem teCCet!4", user, movie, "nice", false);
        reviewRepository.save(review);

        Rating rating = new Rating(4, user, movie, review);
        ratingRepository.save(rating);

        UserLoginDto userLoginDTO = new UserLoginDto("user@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/reviews/" + review.getId(), HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, reviewRepository.findAll().size());
        assertEquals(0, ratingRepository.findAll().size());
        assertEquals(0, movieRepository.findAll().get(0).getRating());
    }

    @Test
    void deleteReviewByIdByModerator() throws Exception {
        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2020 - 1 - 1), "egy megvakult nő képes nézni, nem is akárhogyan, képes visszanézni!");
        movieRepository.save(movie);

        Review review = new Review("nekem teCCet!4", user, movie, "nice", false);
        reviewRepository.save(review);

        Rating rating = new Rating(4, user, movie, review);
        ratingRepository.save(rating);

        User moderator = new User("moderator1", "moderator@email.com", Role.valueOf("Moderator"), passwordEncoder.encode("idontcare"));
        userRepository.save(moderator);
        UserLoginDto userLoginDTO = new UserLoginDto("moderator@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/reviews/" + review.getId(), HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, reviewRepository.findAll().size());
        assertEquals(0, ratingRepository.findAll().size());
        assertEquals(0, movieRepository.findAll().get(0).getRating());
    }


    @Test
    void getReviews() {
        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2020 - 1 - 1), "egy megvakult nő képes nézni, nem is akárhogyan, képes visszanézni!");
        movieRepository.save(movie);

        Movie movie2 = new Movie("A vak asszony visszanéz 2", 0, new Date(2021 - 1 - 1), "az első rész cselekményének rendkívűli folytatása");
        movieRepository.save(movie2);

        Review review = new Review("nekem teCCet!4", user, movie, "nice", false);
        reviewRepository.save(review);

        Rating rating = new Rating(4, user, movie, review);
        ratingRepository.save(rating);

        Review review2 = new Review("az első jobb volt", user, movie2, "mehh", false);
        reviewRepository.save(review2);

        Rating rating2 = new Rating(3, user, movie2, review2);
        ratingRepository.save(rating2);

        ResponseEntity<ReviewListDto> response = restTemplate.getForEntity(baseUrl + port + "/reviews?movieId=" + movie.getId(), ReviewListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, Objects.requireNonNull(response.getBody()).getPageCount());
        assertEquals(0, Objects.requireNonNull(response.getBody()).getPageIndex());
        assertEquals(1, response.getBody().getTotal());
        assertEquals(1, response.getBody().getReviewList().size());
        assertEquals(user.getNickName(), response.getBody().getReviewList().get(0).getCreatorName());
        assertEquals(movie.getTitle(), response.getBody().getReviewList().get(0).getMovieTitle());

        assertEquals(review.getText(), response.getBody().getReviewList().get(0).getText());
        assertEquals(review.getId(), response.getBody().getReviewList().get(0).getId());
        assertEquals(review.getTitle(), response.getBody().getReviewList().get(0).getReviewTitle());
        assertEquals(review.getMovie().getId(), response.getBody().getReviewList().get(0).getMovieId());
        assertEquals(rating.getScore(), response.getBody().getReviewList().get(0).getRating());
    }

    @Test
    void updateReviews() throws Exception {
        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        Movie movie = new Movie("A vak asszony visszanéz", 0, new Date(2020 - 1 - 1), "szuper filmecske");
        movieRepository.save(movie);

        Review review = new Review("szupi egy filmecske", user, movie, "nice", false);
        reviewRepository.save(review);

        Rating rating = new Rating(4, user, movie, review);
        ratingRepository.save(rating);

        ReviewUpdateDto reviewUpdateDto = new ReviewUpdateDto("annyira mégse jó, újra nézve..", "mehh", false, 3);

        UserLoginDto userLoginDTO = new UserLoginDto("user@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<ReviewUpdateDto> request = new HttpEntity<>(reviewUpdateDto, headers);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/reviews/" + review.getId(), HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewUpdateDto.getTitle(), reviewRepository.findAll().get(0).getTitle());
        assertEquals(reviewUpdateDto.getText(), reviewRepository.findAll().get(0).getText());
        assertEquals(reviewUpdateDto.getRating(), ratingRepository.findAll().get(0).getScore());
        assertEquals(reviewUpdateDto.getRating(), movieRepository.findAll().get(0).getRating());
    }
}