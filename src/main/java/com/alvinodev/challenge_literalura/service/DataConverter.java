package com.alvinodev.challenge_literalura.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class DataConverter implements IDataConverter {
    private ObjectMapper objMapper = new ObjectMapper();
    @Override
    public <T> T getData(String json, Class<T> classType) {
        try {
            return objMapper.readValue(json, classType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
