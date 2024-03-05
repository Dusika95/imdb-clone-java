package org.imdb.clone.controllers;

import org.imdb.clone.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2ReviewRepository extends JpaRepository<Review,Long> {
}
