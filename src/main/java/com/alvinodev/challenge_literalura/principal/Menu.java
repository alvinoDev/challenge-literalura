package com.alvinodev.challenge_literalura.principal;

import com.alvinodev.challenge_literalura.dto.ApiResponseDTO;
import com.alvinodev.challenge_literalura.dto.AuthorDataDTO;
import com.alvinodev.challenge_literalura.dto.BookDataDTO;
import com.alvinodev.challenge_literalura.model.Author;
import com.alvinodev.challenge_literalura.model.Book;
import com.alvinodev.challenge_literalura.repository.AuthorRepository;
import com.alvinodev.challenge_literalura.repository.BookRepository;
import com.alvinodev.challenge_literalura.service.ApiConsumer;
import com.alvinodev.challenge_literalura.service.DataConverter;

import java.util.Scanner;

public class Menu {
    private final String URL_BASE = "https://gutendex.com/books";
    private Scanner keyboardInput = new Scanner(System.in);
    private ApiConsumer apiConsumer = new ApiConsumer();
    private DataConverter dataConverter = new DataConverter();
    private String jsonResp;

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    public Menu(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void displayMenu() {
        var option = -1;
        while (option != 0) {
            var menu ="""
            **********************************************
            *           CHALLENGE LITERATURA             *
            **********************************************
            1 - Buscar libro por titulo
            2 - Listar libros registrados
            3 - Listar autores registrados
            4 - Listar autores vivos en un determinado año
            5 - Listar libros por idioma
            0 - Salir
            **********************************************
            Por favor, elije una opcion:
            """;

            System.out.println(menu);
            option = keyboardInput.nextInt();
            keyboardInput.nextLine();

            switch (option) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listLivingAuthorsByYear();
                    break;
                case 5:
                    listBooksByLanguage();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private BookDataDTO getBookData(String titleBook) {
        ApiResponseDTO data = dataConverter.getData(jsonResp, ApiResponseDTO.class);
        return data.bookDataList().stream()
                .filter(b -> b.title().toUpperCase().contains(titleBook.toUpperCase()))
                .findFirst().orElse(null);
    }

    private void searchBookByTitle() {
        System.out.printf("Ingrese el titulo del libro: ");
        String title = keyboardInput.nextLine();

        // Verificar si ya existe en la BD para no consumir la API innecesariamente
        var bookOptional = bookRepository.findFirstByTitleContainsIgnoreCase(title);
        if(bookOptional.isPresent()) {
            System.out.println("\n[!] El libro ya se encuentra registrado en la base de datos:");
            System.out.println(bookOptional.get());
        } else {
            // Si no existe, buscamos en la API
            jsonResp = apiConsumer.getData(URL_BASE + "?search=" + title.replace(" ", "+"));
            BookDataDTO data = getBookData(title);

            if (data != null) {
                // Extraemos los datos del autor desde el DTO
                AuthorDataDTO authorDTO = data.author().get(0);
                // Buscamos si el autor ya existe en nuestra BD para no duplicarlo
                Author author = authorRepository.findFirstByNameContainsIgnoreCase(authorDTO.name())
                        .orElseGet(() -> {
                            // Si no existe, lo creamos Y LO GUARDAMOS explícitamente
                            Author newAuthor = new Author(authorDTO);
                            return authorRepository.save(newAuthor);
                        });

                // Transformamos el DTO a nuestro Modelo 'Book'
                Book book = new Book(data);
                book.setAuthor(author); // Vincula el autor al libro

                bookRepository.save(book); // Registro en la BD (MySQL)
                System.out.println("\n[+] Libro y Autor registrado con éxito:");
                System.out.println(book);
            } else {
                System.out.println("Libro no encontrado.");
            }
        }
    }

    private void listRegisteredBooks() {
        var books = bookRepository.findAll();

        if (books.isEmpty()) {
            System.out.println("\n[!] No hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n" + "─".repeat(90));
            System.out.println("                              LISTA DE LIBROS REGISTRADOS");
            System.out.println("─".repeat(90));
            System.out.printf("%-4s │ %-40s │ %-30s │ %-10s%n", "Nº", "TÍTULO", "AUTOR", "IDIOMA");
            System.out.println("-".repeat(90));

            for (int i = 0; i < books.size(); i++) {
                var b = books.get(i);
                System.out.printf("[%d] │ %-40.40s │ %-30.30s │ %-10s%n", (i + 1), b.getTitle(), (b.getAuthor() != null ? b.getAuthor().getName() : "Autor Desconocido"), b.getLanguage());
            }

            System.out.println("─".repeat(90));
            System.out.printf("TOTAL DE LIBROS: %d%n", books.size());
            System.out.println("─".repeat(90) + "\n");
        }
    }

    private void listRegisteredAuthors() {
        var authors = authorRepository.findAll();

        if (authors.isEmpty()) {
            System.out.println("\n[!] No hay autores registrados.");
        } else {
            System.out.println("\n" + "─".repeat(100));
            System.out.println("                              LISTA DE AUTORES REGISTRADOS");
            System.out.println("─".repeat(100));

            authors.forEach(a -> {
                System.out.println("Autor: " + a.getName());
                System.out.println("Fecha de nacimiento: " + (a.getBirthYear() != null ? a.getBirthYear() : "N/A"));
                System.out.println("Fecha de fallecimiento: " + (a.getDeathYear() != null ? a.getDeathYear() : "N/A"));

                // Obtenemos la lista de títulos
                String libros = a.getBooks().stream()
                        .map(Book::getTitle)
                        .collect(java.util.stream.Collectors.joining(" │ "));

                System.out.println("Libros: [ " + libros + " ]");
                System.out.println("-".repeat(80));
            });

            System.out.printf("TOTAL DE AUTORES: %d%n", authors.size());
            System.out.println("─".repeat(100) + "\n");
        }
    }

    private void listLivingAuthorsByYear() { }
    private void listBooksByLanguage() { }
}
