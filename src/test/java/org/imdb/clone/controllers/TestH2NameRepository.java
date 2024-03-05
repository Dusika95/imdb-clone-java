package org.imdb.clone.controllers;

import org.imdb.clone.models.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface TestH2NameRepository extends JpaRepository<Name,Long> {

}
