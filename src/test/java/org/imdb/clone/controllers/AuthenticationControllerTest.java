package org.imdb.clone.controllers;


import org.imdb.clone.DTOs.UserLoginDto;
import org.imdb.clone.DTOs.UserLoginResponseDto;
import org.imdb.clone.DTOs.UserRegisterDto;
import org.imdb.clone.models.Role;
import org.imdb.clone.models.User;
import org.imdb.clone.repository.Impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthenticationControllerTest {
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
    private BCryptPasswordEncoder passwordEncoder;
    private static RestTemplate restTemplate;
    @Autowired
    AuthServiceImpl authService;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @Test
    void register() {
        UserRegisterDto userRegisterDTO = new UserRegisterDto("user", "user@email.com", "idontcare", "idontcare");

        ResponseEntity<UserRegisterDto> response = restTemplate.postForEntity(baseUrl + port + "/register", userRegisterDTO, UserRegisterDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userRegisterDTO.getEmail(), userRepository.findAll().get(1).getEmail());
        assertEquals(userRegisterDTO.getNickName(), userRepository.findAll().get(1).getNickName());
        assertTrue(passwordEncoder.matches(userRegisterDTO.getPassword(), userRepository.findAll().get(1).getPassword()));
    }


    @Test
    void Login() {
        User user = new User("user", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        UserLoginDto userLoginDto= new UserLoginDto("user@email.com","idontcare");

        ResponseEntity<UserLoginResponseDto> response = restTemplate.postForEntity(baseUrl + port + "/login", userLoginDto, UserLoginResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getEmail(), Objects.requireNonNull(response.getBody()).getEmail());
        assertEquals(user.getNickName(),response.getBody().getName());
        assertEquals(user.getId(),response.getBody().getId());
        assertEquals(user.getRole().getValue(),response.getBody().getRole().getValue());
        assertNotNull(response.getBody().getJwtToken());

    }


}