package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.RatingCreateDto;
import org.imdb.clone.DTOs.RatingUpdateDto;
import org.imdb.clone.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/ratings")
    public ResponseEntity postRating(@Valid @RequestBody RatingCreateDto ratingCreateDto) throws Exception {
            ratingService.postRating(ratingCreateDto);
            return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/ratings/{id}")
    public ResponseEntity deleteRating(@PathVariable Long id) {
        try {
            ratingService.deleteRating(id);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/ratings")
    public ResponseEntity getRatingByUserIdAndMovieId(@RequestParam Long movieId) {
        try {
            return ResponseEntity.status(200).body(ratingService.getRatingByUserIdAndMovieId(movieId));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/ratings/{id}")
    public ResponseEntity updateRating(@PathVariable Long id, @Valid @RequestBody RatingUpdateDto ratingUpdateDto) {
        try {
            ratingService.updateRating(id, ratingUpdateDto);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
