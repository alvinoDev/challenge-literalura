package com.alvinodev.challenge_literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiResponseDTO (@JsonAlias("results") List<BookDataDTO> bookDataList) { }
//@jsonIgnore20222