package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.UserUpdateDto;
import org.imdb.clone.DTOs.UserLoginDto;
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

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProfileControllerTest {

    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost:";
    @Autowired
    TestH2UserRepository userRepository;
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
    void updateProfile() throws Exception {
        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);
        UserLoginDto userLoginDTO = new UserLoginDto("user@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        UserUpdateDto userUpdateDto = new UserUpdateDto("changedEmail@email.com", "changedPassword", "changedPassword");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<UserUpdateDto> request = new HttpEntity<>(userUpdateDto, headers);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/profile", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userUpdateDto.getEmail(),userRepository.findAll().get(1).getEmail());
        assertTrue(passwordEncoder.matches(userUpdateDto.getPassword(), userRepository.findAll().get(1).getPassword()));
    }
    @Test

    void updateProfileEmail() throws Exception {
        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);
        UserLoginDto userLoginDTO = new UserLoginDto("user@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        UserUpdateDto userUpdateDto = new UserUpdateDto("changed@email.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<UserUpdateDto> request = new HttpEntity<>(userUpdateDto, headers);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/profile", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userUpdateDto.getEmail(),userRepository.findAll().get(1).getEmail());
        assertTrue(passwordEncoder.matches("idontcare", userRepository.findAll().get(1).getPassword()));
    }

    @Test
    void deleteProfile() throws Exception{
        User user = new User("user1", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        UserLoginDto userLoginDTO = new UserLoginDto("user@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/profile", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1,userRepository.findAll().size());
    }
}