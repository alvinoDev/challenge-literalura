package com.alvinodev.challenge_literalura.model;

import com.alvinodev.challenge_literalura.dto.BookDataDTO;

import java.util.stream.Collectors;

public class Book {
    private Long id;
    private String title;
    private String author;
    private String language;
    private Double downloadCount;

    public Book() {}

    public Book(BookDataDTO bookDataDTO) {
        this.title = bookDataDTO.title();
        // Transformamos la lista de autores a un solo String
        this.author = bookDataDTO.author().stream()
                .map(a -> a.name())
                .collect(Collectors.joining(", "));
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
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
            """, title, author, language, downloadCount);
    }
}
