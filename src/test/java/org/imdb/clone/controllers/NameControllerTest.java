package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.*;
import org.imdb.clone.models.Name;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class NameControllerTest {
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost:";
    @Autowired
    TestH2NameRepository nameRepository;
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
    void saveName() throws Exception {
        User editor = new User("editor", "editor@email.com", Role.valueOf("Editor"), passwordEncoder.encode("idontcare"));
        userRepository.save(editor);

        NameCreateDto nameCreateDto = new NameCreateDto("Kis Jóska Pista", "valamit nagyon tud");

        UserLoginDto userLoginDTO = new UserLoginDto("editor@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<NameCreateDto> request = new HttpEntity<>(nameCreateDto, headers);

        ResponseEntity<NameCreateDto> response = restTemplate.postForEntity(baseUrl + port + "/names", request, NameCreateDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, nameRepository.findAll().get(0).getId());
        assertEquals(nameCreateDto.getFullName(), nameRepository.findAll().get(0).getFullName());
        assertEquals(nameCreateDto.getDescription(), nameRepository.findAll().get(0).getDescription());
    }

    @Test
    void getAllName() {
        Name name = new Name("Kis Jóska Pista", "valamit nagyon tud");
        nameRepository.save(name);

        ResponseEntity<NameDto[]> response = restTemplate.getForEntity(baseUrl + port + "/names", NameDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, nameRepository.findAll().size());
        assertEquals(name.getFullName(), nameRepository.findAll().get(0).getFullName());
        assertEquals(name.getId(), nameRepository.findAll().get(0).getId());
    }

    @Test
    void getNameById() {
        Name name = new Name("Kis Jóska Pista", "valamit nagyon tud");
        nameRepository.save(name);

        ResponseEntity<NameDetailsDto> response = restTemplate.getForEntity(baseUrl + port + "/names/" + name.getId(), NameDetailsDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(name.getId(), Objects.requireNonNull(response.getBody()).getId());
        assertEquals(name.getFullName(), response.getBody().getFullName());
        assertEquals(name.getDescription(), response.getBody().getDescription());
    }

    @Test
    void updateName() throws Exception {
        User editor = new User("editor", "editor@email.com", Role.valueOf("Editor"), passwordEncoder.encode("idontcare"));
        userRepository.save(editor);

        Name name = new Name("Kis Jóska Pista", "valamit nagyon tud");
        nameRepository.save(name);

        NameCreateDto nameCreateDto = new NameCreateDto("Kis Jóska Pista ifjabb", "valamit nagyon tud");

        UserLoginDto userLoginDTO = new UserLoginDto("editor@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<NameCreateDto> request = new HttpEntity<>(nameCreateDto, headers);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/names/" + name.getId(), HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nameCreateDto.getDescription(), nameRepository.findAll().get(0).getDescription());
        assertEquals(nameCreateDto.getFullName(), nameRepository.findAll().get(0).getFullName());
    }
}