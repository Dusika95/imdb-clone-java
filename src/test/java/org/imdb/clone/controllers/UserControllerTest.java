package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.InternalUserCreateDto;
import org.imdb.clone.DTOs.UserLoginDto;
import org.imdb.clone.DTOs.InternalRoleDto;
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
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {
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
    void createInternalUser() throws Exception {
        UserLoginDto userLoginDTO = new UserLoginDto("admin@example.com", "adminpassword");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        InternalUserCreateDto internalUserCreateDto = new InternalUserCreateDto(
                "editor", "editor@editor.com", InternalRoleDto.valueOf("Editor"), "idontcare", "idontcare");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<InternalUserCreateDto> request = new HttpEntity<>(internalUserCreateDto, headers);

        ResponseEntity<InternalUserCreateDto> response = restTemplate.exchange(baseUrl + port + "/users", HttpMethod.POST, request, InternalUserCreateDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2, userRepository.findAll().size());
        assertEquals(internalUserCreateDto.getRole().getValue(), userRepository.findAll().get(1).getRole().getValue());
        assertEquals(internalUserCreateDto.getEmail(), userRepository.findAll().get(1).getEmail());
        assertEquals(internalUserCreateDto.getNickName(), userRepository.findAll().get(1).getNickName());
        assertTrue(passwordEncoder.matches(internalUserCreateDto.getPassword(), userRepository.findAll().get(1).getPassword()));
    }

    @Test
    void deleteUser() throws Exception {
        User moderator = new User("moderator", "moderator@email.com", Role.valueOf("Moderator"), passwordEncoder.encode("idontcare"));
        userRepository.save(moderator);

        User user = new User("user", "user@email.com", Role.valueOf("User"), passwordEncoder.encode("idontcare"));
        userRepository.save(user);

        UserLoginDto userLoginDTO = new UserLoginDto("moderator@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/users/" + user.getId(), HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2,userRepository.findAll().size());
    }

    //itt most kéne csinálnom egy admint? de alapból kreálodik egy minden futásnál
    @Test
    void deleteAdmin() throws Exception{
        User moderator = new User("moderator", "moderator@email.com", Role.valueOf("Moderator"), passwordEncoder.encode("idontcare"));
        userRepository.save(moderator);

        UserLoginDto userLoginDTO = new UserLoginDto("moderator@email.com", "idontcare");
        String token = authService.validateUser(userLoginDTO).getJwtToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + port + "/users/1", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        //nem jó
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(2,userRepository.findAll().size());
    }

}