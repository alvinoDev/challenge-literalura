package com.alvinodev.challenge_literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDataDTO(
        @JsonAlias("title") String title,
//        @JsonAlias("summaries") List<String> summaries,
        @JsonAlias("authors") List<AuthorDataDTO> author,
        @JsonAlias("languages") List<String> languages,
        @JsonAlias("download_count") Double download_count) {
}
