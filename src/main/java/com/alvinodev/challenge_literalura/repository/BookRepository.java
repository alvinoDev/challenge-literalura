package com.alvinodev.challenge_literalura.repository;

import com.alvinodev.challenge_literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Buscar libros por título ignorando mayúsculas para evitar duplicados
    Optional<Book> findFirstByTitleContainsIgnoreCase(String title); // Consulta usando convenciones de JPA

    // Buscar libros por idioma
    @Query("SELECT b FROM Book b WHERE b.language = :language")
    List<Book> findByLanguage(String language);

    // RETO EXTRA: Mostrar Top 10 Libros más descargados
    @Query("SELECT b FROM Book b ORDER BY b.downloadCount DESC LIMIT 10")
    List<Book> findTop10ByOrderByDownloadCountDesc();
}
