package br.com.JonasSMendes.screenmatch.service;

public interface IConverteDados {

    <T> T obterdados(String json, Class<T> classe);
}
