package br.com.JonasSMendes.screenmatch.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.LocalDate;

public record EpisodioDTO(String titulo,
                          Integer numeroEpisodio,
                          Integer temporada
                          ) {
}
