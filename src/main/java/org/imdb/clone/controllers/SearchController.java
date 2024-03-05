package org.imdb.clone.controllers;

import org.imdb.clone.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam String searchType, @RequestParam String searchText, @RequestParam(defaultValue = "0") int moviePageIndex, @RequestParam(defaultValue = "0") int namePageIndex) {
            return ResponseEntity.status(200).body(searchService.search(searchText, searchType, moviePageIndex, namePageIndex));
    }
}
