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

import java.util.DoubleSummaryStatistics;
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
            6 - Generar estadísticas
            7 - Top 10 libros más descargados
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
                case 6:
                    displayStatistics();
                    break;
                case 7:
                    listTop10();
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
                System.out.println("\n[!] Libro no encontrado.");
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

            System.out.println(".".repeat(90));
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

    private void listLivingAuthorsByYear() {
        System.out.print("Ingrese el año para consultar autores vivos: ");
        var year = keyboardInput.nextInt();
        keyboardInput.nextLine();

        var authors = authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);

        if (authors.isEmpty()) {
            System.out.println("\n[!] No se encontraron autores vivos en el año " + year);
        } else {
            System.out.println("\n" + "─".repeat(60));
            System.out.println("                AUTORES VIVOS EN EL AÑO " + year);
            System.out.println("─".repeat(60));

            authors.forEach(a -> System.out.printf("• %s [%s - %s]%n",
                    a.getName(), a.getBirthYear(), a.getDeathYear()));

            System.out.println(".".repeat(60));
            System.out.printf("TOTAL DE AUTORES VIVOS: %d%n", authors.size());
            System.out.println("─".repeat(60) + "\n");
        }
    }

    private void listBooksByLanguage() {
        System.out.println("""
            Ingrese el código del idioma para buscar los libros:
            [es] - Español
            [en] - Inglés
            [fr] - Francés
            [pt] - Portugués
            """);
        String languageCode = keyboardInput.nextLine().toLowerCase();

        var books = bookRepository.findByLanguage(languageCode);

        if (books.isEmpty()) {
            System.out.println("\n[!] No se encontraron libros en el idioma: " + languageCode);
        } else {
            System.out.println("\n" + "─".repeat(90));
            System.out.println("                               LIBROS EN IDIOMA [" + languageCode.toUpperCase() + "]");
            System.out.println("─".repeat(90));
            System.out.printf("%-3s │ %-40s │ %-30s │ %-10s%n", "Nº", "TÍTULO", "AUTOR", "DESCARGAS");
            System.out.println("-".repeat(90));

            for (int i = 0; i < books.size(); i++) {
                var b = books.get(i);
                String authorName = (b.getAuthor() != null) ? b.getAuthor().getName() : "Desconocido";
                System.out.printf("[%d] │ %-40.40s │ %-30.30s │ %-10.0f%n",
                        (i + 1), b.getTitle(), authorName, b.getDownloadCount());
            }
            System.out.println("─".repeat(90) + "\n");
        }
    }

    // RETOS EXTRA DEL CHALLENGE
    private void displayStatistics(){
        var books = bookRepository.findAll();
        if(books.isEmpty()) {
            System.out.println("No hay datos para generar estadísticas.");
            return;
        }

        DoubleSummaryStatistics stats = books.stream().mapToDouble(Book::getDownloadCount).summaryStatistics();

        System.out.println("\n" + "─".repeat(40));
        System.out.println("       ESTADÍSTICAS DE DESCARGAS");
        System.out.println("─".repeat(40));
        System.out.printf("Media: %.2f%n", stats.getAverage());
        System.out.printf("Máxima: %.0f%n", stats.getMax());
        System.out.printf("Mínima: %.0f%n", stats.getMin());
        System.out.println(".".repeat(40));
        System.out.printf("TOTAL DE REGISTROS EVALUADOS: %d%n", stats.getCount());
        System.out.println("─".repeat(40) + "\n");
    }

    private void listTop10(){
        var topBooks = bookRepository.findTop10ByOrderByDownloadCountDesc();
        // System.out.println("\n--- TOP 10 LIBROS MÁS DESCARGADOS ---");
        System.out.println("\n" + "─".repeat(64));
        System.out.println("              TOP 10 LIBROS MÁS DESCARGADOS");
        System.out.println("─".repeat(64));

        topBooks.forEach(b -> System.out.printf("%-40.40s | Descargas: %.0f%n", b.getTitle(), b.getDownloadCount()));
        System.out.println("─".repeat(64) + "\n");
    }
}
