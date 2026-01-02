package com.alvinodev.challenge_literalura.model;

import com.alvinodev.challenge_literalura.dto.AuthorDataDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books= new ArrayList<>();

    public Author() {}

    public Author(AuthorDataDTO d) {
        this.name = d.name();
        this.birthYear = d.birthYear() != null ? Integer.valueOf(d.birthYear()) : null;
        this.deathYear = d.deathYear() != null ? Integer.valueOf(d.deathYear()) : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getNameBooks() {
        return books.stream().map(Book::getTitle).collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        //        return "Author{" +
        //                "id=" + id +
        //                ", name='" + name + '\'' +
        //                ", birthYear=" + birthYear +
        //                ", deathYear=" + deathYear +
        //                ", books=" + books +
        //                '}';
        return String.format("""
            ┌─────────────────────────────────────────────┐
            │               DATOS DEL AUTOR               │
            ├─────────────────────────────────────────────┘
            │ Autor: %s
            │ Fecha de Nacimiento: %s
            │ Fecha de fallecimiento: %s
            │ Libros: %s
            └─────────────────────────────────────────────
            """, name, birthYear, deathYear, getNameBooks());
    }
}
