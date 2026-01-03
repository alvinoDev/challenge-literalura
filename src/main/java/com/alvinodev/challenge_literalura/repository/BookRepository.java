package com.alvinodev.challenge_literalura.repository;

import com.alvinodev.challenge_literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Buscamos por título ignorando mayúsculas para evitar duplicados
    Optional<Book> findFirstByTitleContainsIgnoreCase(String title); // Consulta usando convenciones de JPA

    @Query("SELECT b FROM Book b WHERE b.language = :language")
    List<Book> findByLanguage(String language);
}
