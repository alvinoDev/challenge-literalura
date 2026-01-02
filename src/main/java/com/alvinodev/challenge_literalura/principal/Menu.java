package com.alvinodev.challenge_literalura.principal;

import com.alvinodev.challenge_literalura.dto.ApiResponseDTO;
import com.alvinodev.challenge_literalura.dto.BookDataDTO;
import com.alvinodev.challenge_literalura.model.Book;
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

    private BookRepository repository;
    public Menu(BookRepository repository) {
        this.repository = repository;
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
        var bookOptional = repository.findByTitleContainsIgnoreCase(title);
        if(bookOptional.isPresent()) {
            System.out.println("\n[!] El libro ya se encuentra registrado en la base de datos:");
            System.out.println(bookOptional.get());
        } else {
            // Si no existe, buscamos en la API
            jsonResp = apiConsumer.getData(URL_BASE + "?search=" + title.replace(" ", "+"));
            BookDataDTO data = getBookData(title);

            if (data != null) {
                // Transformamos el DTO a nuestro Modelo 'Book'
                Book book = new Book(data);
                repository.save(book); // Registro en la BD (MySQL)
                System.out.println("\n[+] Libro registrado con éxito:");
                System.out.println(book);
            } else {
                System.out.println("Libro no encontrado.");
            }
        }
    }

    //    private void listRegisteredBooks() {
    //        var books = repository.findAll();
    //
    //        if (books.isEmpty()) {
    //            System.out.println("No hay libros registrados aún.");
    //        } else {
    //            System.out.println("\n--- LISTA DE LIBROS REGISTRADOS ---");
    //            // Usamos el formato toString() de la entidad Book
    //            books.forEach(System.out::println);
    //        }
    //    }

    private void listRegisteredBooks() {
        var books = repository.findAll();

        if (books.isEmpty()) {
            System.out.println("\n[!] No hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n" + "─".repeat(90));
            System.out.println("                              LISTA DE LIBROS REGISTRADOS");
            System.out.println("─".repeat(90));
            System.out.printf("%-3s │ %-40s │ %-30s │ %-10s%n", "Nº", "TÍTULO", "AUTOR", "IDIOMA");
            System.out.println("-".repeat(90));

            // books.forEach(b -> System.out.printf("%-40.40s │ %-30.30s │ %-10s%n", b.getTitle(), b.getAuthor(), b.getLanguage()));
            // System.out.println("─".repeat(84) + "\n");
            for (int i = 0; i < books.size(); i++) {
                var b = books.get(i);
                System.out.printf("[%d] │ %-40.40s │ %-30.30s │ %-10s%n", (i + 1), b.getTitle(), b.getAuthor(), b.getLanguage());
            }

            System.out.println("─".repeat(90));
            System.out.printf("TOTAL DE LIBROS: %d%n", books.size());
            System.out.println("─".repeat(90) + "\n");
        }
    }

    private void listRegisteredAuthors() { }
    private void listLivingAuthorsByYear() { }
    private void listBooksByLanguage() { }
}
