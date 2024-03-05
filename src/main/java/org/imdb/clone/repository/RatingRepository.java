package org.imdb.clone.repository;

import org.imdb.clone.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    List<Rating> findByMovieId(Long movieId);
    @Query("SELECT r FROM Rating r WHERE r.user.id = :userId AND r.movie.id = :movieId")
    Rating findByUserIdAndMovieId(@Param("userId") Long userId, @Param("movieId") Long movieId);
    Rating findByReviewId(Long reviewId);
    List<Rating> findByUserId(Long userId);
}
