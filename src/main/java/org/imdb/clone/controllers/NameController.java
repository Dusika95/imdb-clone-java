package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.NameCreateDto;
import org.imdb.clone.services.NameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class NameController {
    private final NameService nameService;

    @Autowired
    public NameController(NameService nameService) {
        this.nameService = nameService;
    }

    @PostMapping("/names")
    public ResponseEntity saveName(@Valid @RequestBody NameCreateDto nameCreateDto) {

            nameService.createName(nameCreateDto);
            return ResponseEntity.status(200).build();

    }

    @GetMapping("/names")
    public ResponseEntity getAllName() {
            return ResponseEntity.status(200).body(nameService.getAllName());
    }

    @GetMapping("/names/{id}")
    public ResponseEntity getNameById(@PathVariable Long id) throws Exception {
            return ResponseEntity.status(200).body(nameService.getNameById(id));
    }

    @PutMapping("/names/{id}")
    public ResponseEntity updateName(@PathVariable Long id, @RequestBody NameCreateDto nameCreateDto) throws Exception{
            nameService.updateName(id, nameCreateDto);
            return ResponseEntity.status(200).build();
    }
}
