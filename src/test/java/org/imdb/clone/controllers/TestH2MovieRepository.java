package org.imdb.clone.controllers;

import org.imdb.clone.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface TestH2MovieRepository extends JpaRepository<Movie,Long> {

}
