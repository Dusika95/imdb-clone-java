package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.UserUpdateDto;
import org.imdb.clone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class ProfileController {
    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity getProfile(){
            return ResponseEntity.status(200).body(userService.getProfile());
    }
    @PutMapping("/profile")
    public ResponseEntity updateProfile(@Valid @RequestBody UserUpdateDto UserUpdateDto) {
            userService.updateUser(UserUpdateDto);
            return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/profile")
    public ResponseEntity deleteProfile() {
            userService.deleteProfile();
            return ResponseEntity.status(200).build();
    }
}
