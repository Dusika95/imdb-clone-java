package org.imdb.clone.controllers;

import org.imdb.clone.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2RatingRepository extends JpaRepository<Rating,Long> {
}
