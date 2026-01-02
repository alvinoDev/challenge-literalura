package com.alvinodev.challenge_literalura.repository;

import com.alvinodev.challenge_literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Buscamos por título ignorando mayúsculas para evitar duplicados
    Optional<Book> findByTitleContainsIgnoreCase(String title);
}
