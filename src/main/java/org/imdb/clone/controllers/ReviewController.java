package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.ReviewCreateDto;
import org.imdb.clone.DTOs.ReviewUpdateDto;
import org.imdb.clone.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    public ResponseEntity postReview(@Valid @RequestBody ReviewCreateDto reviewCreateDto) throws Exception{
            reviewService.postReview(reviewCreateDto);
            return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity deleteReviewById(@PathVariable Long id) throws Exception{
            reviewService.deleteReviewById(id);
            return ResponseEntity.status(200).build();
    }

    @GetMapping("/reviews")
    public ResponseEntity getReviews(@RequestParam(defaultValue = "0") Long userId, @RequestParam(defaultValue = "0") Long movieId,
                                     @RequestParam(defaultValue = "false") boolean hideSpoilers, @RequestParam(defaultValue = "0") int pageIndex,
                                     @RequestParam(defaultValue = "10") int pageCount) {
            return ResponseEntity.status(200).body(reviewService.getReviews(userId, movieId, hideSpoilers, pageIndex, pageCount));
    }

    @PutMapping("reviews/{id}")
    public ResponseEntity updateReviews(@Valid @RequestBody ReviewUpdateDto reviewUpdateDto, @PathVariable Long id) throws Exception{
            reviewService.updateReview(id, reviewUpdateDto);
            return ResponseEntity.status(200).build();
    }
}
