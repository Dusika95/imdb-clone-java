package org.imdb.clone.controllers;

import org.imdb.clone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface TestH2UserRepository extends JpaRepository<User, Long> {

}
