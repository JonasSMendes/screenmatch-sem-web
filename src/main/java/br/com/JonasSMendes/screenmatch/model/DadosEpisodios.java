package br.com.JonasSMendes.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodios(@JsonAlias("Title") String titulo,
                             @JsonAlias("Episode") Integer numeroEpisodio,
                             @JsonAlias("imdbRating") String avaliacao,
                             @JsonAlias("Released") String dataLancamento) {


    @Override
    public String toString() {
        return """
               (Episodios : %s , titulo : %s ,  Avaliação : %s , data de lançamento : %s)
                """.formatted( numeroEpisodio, titulo , avaliacao, dataLancamento);
    }
}
