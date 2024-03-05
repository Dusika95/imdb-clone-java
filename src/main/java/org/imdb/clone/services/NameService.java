package org.imdb.clone.services;

import org.imdb.clone.DTOs.NameDto;
import org.imdb.clone.DTOs.NameDetailsDto;
import org.imdb.clone.DTOs.NameCreateDto;


import java.util.List;

public interface NameService {
    public void createName(NameCreateDto nameCreateDto);
    public List<NameDto> getAllName();
    public NameDetailsDto getNameById(Long id) throws Exception;
    public void updateName(Long id, NameCreateDto nameCreateDto) throws Exception;
}
