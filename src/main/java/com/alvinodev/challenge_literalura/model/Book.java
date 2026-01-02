package com.alvinodev.challenge_literalura.model;

import com.alvinodev.challenge_literalura.dto.BookDataDTO;
import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    // private String author;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id") // Esto crea la Clave Foránea en la tabla books
    private Author author;

    private String language;
    private Double downloadCount;

    public Book() {}

    public Book(BookDataDTO bookDataDTO) {
        this.title = bookDataDTO.title();
        // Tomamos el primer idioma de la lista
        this.language = bookDataDTO.languages().isEmpty() ? "Unknown" : bookDataDTO.languages().get(0);
        this.downloadCount = bookDataDTO.download_count();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Double downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return String.format("""
            ┌─────────────────────────────────────────────┐
            │               DATOS DEL LIBRO               │
            ├─────────────────────────────────────────────┘
            │ Título: %s
            │ Autor: %s
            │ Idioma: %s
            │ Descargas: %.0f
            └─────────────────────────────────────────────
            """, title, (author != null ? author.getName() : "Desconocido"), language, downloadCount);
    }
}
