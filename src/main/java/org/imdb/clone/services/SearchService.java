package org.imdb.clone.services;

import org.imdb.clone.DTOs.SearchListDto;

public interface SearchService {
    SearchListDto search(String searchText, String searchType, int moviePageIndex, int namePageIndex);
}
