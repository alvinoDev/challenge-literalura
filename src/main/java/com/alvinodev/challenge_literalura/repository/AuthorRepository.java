package com.alvinodev.challenge_literalura.repository;

import com.alvinodev.challenge_literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    // Buscamos por name ignorando mayúsculas para evitar duplicados
    Optional<Author> findFirstByNameContainsIgnoreCase(String name);

    List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(Integer birthYear, Integer deathYear);
}
