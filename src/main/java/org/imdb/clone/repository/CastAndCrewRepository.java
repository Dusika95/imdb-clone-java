package org.imdb.clone.repository;

import org.imdb.clone.DTOs.CastAndCrewWithNameDto;
import org.imdb.clone.models.CastAndCrew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CastAndCrewRepository extends JpaRepository<CastAndCrew, Long> {

    @Query("SELECT new org.imdb.clone.DTOs.CastAndCrewWithNameDto(cc.name.id, cc.role, cc.name.fullName, cc.name.description) FROM CastAndCrew cc JOIN cc.name WHERE cc.movie.id = :movieId")
    List<CastAndCrewWithNameDto> findCrewWithNamesByMovieId(@Param("movieId") Long movieId);

    List<CastAndCrew> findByNameId(Long nameId);

    void deleteByMovieId(Long movieId);
}
