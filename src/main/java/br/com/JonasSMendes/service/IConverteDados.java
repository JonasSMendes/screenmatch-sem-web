package br.com.JonasSMendes.service;

public interface IConverteDados {

    <T> T obterdados(String json, Class<T> classe);
}
