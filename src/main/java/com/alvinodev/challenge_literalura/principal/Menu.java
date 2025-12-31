package com.alvinodev.challenge_literalura.principal;

import com.alvinodev.challenge_literalura.service.ApiConsumer;
import com.alvinodev.challenge_literalura.service.DataConverter;

import java.util.Scanner;

public class Menu {
    private final String URL_BASE = "https://gutendex.com/books";
    private Scanner keyboardInput = new Scanner(System.in);
    private ApiConsumer apiConsumer = new ApiConsumer();
    private DataConverter dataConverter = new DataConverter();
    private String jsonResp;

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

    private void searchBookByTitle() { }
    private void listRegisteredBooks() { }
    private void listRegisteredAuthors() { }
    private void listLivingAuthorsByYear() { }
    private void listBooksByLanguage() { }
}
