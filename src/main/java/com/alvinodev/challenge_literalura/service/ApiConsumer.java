package com.alvinodev.challenge_literalura.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class ApiConsumer {
    public String getData(String url) {
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String json = response.body();
                return json;
            } else {
                System.out.println("Error en la API. Código de estado: " + response.statusCode());
                return "";
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al conectar con la API: " + e.getMessage());
        }
    }
}
