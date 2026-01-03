package com.alvinodev.challenge_literalura.repository;

import com.alvinodev.challenge_literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    // Buscar por name ignorando mayúsculas para evitar duplicados
    Optional<Author> findFirstByNameContainsIgnoreCase(String name);

    // Buscar fecha entre F. Nac. y F. Fa.
    List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(Integer birthYear, Integer deathYear);

    // Buscar Autor por nombre
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Author> findByName(String name);
}
