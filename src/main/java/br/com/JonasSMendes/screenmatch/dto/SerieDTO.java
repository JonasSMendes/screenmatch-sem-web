package br.com.JonasSMendes.screenmatch.dto;

import br.com.JonasSMendes.screenmatch.model.Categoria;


public record SerieDTO( Long id,
         String titulo,
         Integer totalTemporadas,
         Double avaliacao,
         Categoria genero,
         String atores,
         String img,
         String sinopse) {
}
