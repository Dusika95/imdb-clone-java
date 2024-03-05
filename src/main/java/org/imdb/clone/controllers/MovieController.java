package org.imdb.clone.controllers;

import org.imdb.clone.DTOs.MovieCreateDto;
import org.imdb.clone.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public ResponseEntity getAllMovies(@RequestParam(defaultValue = "0") int pageIndex) {
            return ResponseEntity.status(200).body(movieService.getAllMovies(pageIndex));
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity getMovieDetailsById(@PathVariable Long id) throws Exception {
            return ResponseEntity.status(200).body(movieService.getMovieById(id));
    }

    @PostMapping("/movies")
    public ResponseEntity postMovie(@Valid @RequestBody MovieCreateDto movieCreateDto) throws Exception {
            movieService.createMovie(movieCreateDto);
            return ResponseEntity.status(200).build();
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity updateMovie(@Valid @RequestBody MovieCreateDto movieCreateDto, @PathVariable Long id) throws Exception {
            movieService.updateMovie(movieCreateDto, id);
            return ResponseEntity.status(200).build();
    }
}

