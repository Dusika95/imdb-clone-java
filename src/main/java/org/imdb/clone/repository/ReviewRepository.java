package org.imdb.clone.repository;


import org.imdb.clone.DTOs.ReviewDto;
import org.imdb.clone.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT new org.imdb.clone.DTOs.ReviewDto(r.id, m.id, u.nickName, m.title, r.text, r.title, r.hasSpoiler, ra.score) " +
            "FROM Review r " +
            "INNER JOIN r.user u " +
            "INNER JOIN r.movie m " +
            "INNER JOIN r.ratings ra " +
            "WHERE u.id = :userId AND m.id = :movieId")
    Page<ReviewDto> findByUserIdAndMovieId(Long userId, Long movieId, Pageable pageable);

    @Query("SELECT new org.imdb.clone.DTOs.ReviewDto(r.id, m.id, u.nickName, m.title, r.text, r.title, r.hasSpoiler, ra.score) " +
            "FROM Review r " +
            "INNER JOIN r.user u " +
            "INNER JOIN r.movie m " +
            "INNER JOIN r.ratings ra " +
            "WHERE u.id = :userId")
    Page<ReviewDto> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT new org.imdb.clone.DTOs.ReviewDto(r.id, m.id, u.nickName, m.title, r.text, r.title, r.hasSpoiler, ra.score) " +
            "FROM Review r " +
            "INNER JOIN r.user u " +
            "INNER JOIN r.movie m " +
            "INNER JOIN r.ratings ra " +
            "WHERE m.id = :movieId")
    Page<ReviewDto> findByMovieId(Long movieId, Pageable pageable);
}
