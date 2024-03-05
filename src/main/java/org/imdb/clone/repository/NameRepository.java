package org.imdb.clone.repository;

import org.imdb.clone.models.Name;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {
    @Query("SELECT n FROM Name n WHERE LOWER(n.fullName) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    Page<Name> searchNamesByFullName(@Param("searchText") String searchText, Pageable pageable);
}
