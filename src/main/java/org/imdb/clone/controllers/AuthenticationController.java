package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.UserLoginDto;
import org.imdb.clone.DTOs.UserRegisterDto;
import org.imdb.clone.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;


@RestController
public class AuthenticationController {
    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody(required = true) UserRegisterDto userRegisterDto) throws Exception{
            authService.registerUser(userRegisterDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity validateUser(@Valid @RequestBody UserLoginDto userLoginDto) throws Exception {
        return ResponseEntity.ok().body(authService.validateUser(userLoginDto));
    }
}
