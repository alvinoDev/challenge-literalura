package com.alvinodev.challenge_literalura.service;

public interface IDataConverter {
    <T> T getData(String json, Class<T> classType);
}
