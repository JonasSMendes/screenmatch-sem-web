package br.com.JonasSMendes.screenmatch.service;

import java.util.List;

public interface IConverteDados {

    <T> T obterdados(String json, Class<T> classe);
}
