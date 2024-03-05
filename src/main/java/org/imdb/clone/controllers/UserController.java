package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.InternalUserCreateDto;
import org.imdb.clone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity createInternalUser(@RequestBody InternalUserCreateDto internalUserCreateDto) throws Exception{
            userService.createInternalUser(internalUserCreateDto);
            return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) throws Exception{
            userService.deleteUser(id);
            return ResponseEntity.status(200).build();
    }

}
