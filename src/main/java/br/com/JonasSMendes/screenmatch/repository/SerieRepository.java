package br.com.JonasSMendes.screenmatch.repository;

import br.com.JonasSMendes.screenmatch.model.Categoria;
import br.com.JonasSMendes.screenmatch.model.Episodio;
import br.com.JonasSMendes.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeSerie, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalTemporada, Double avaliacao);

    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporada AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaAvaliacao(Integer totalTemporada, Double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodioPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> top5EpisodiosDasSeries(Serie serie);
}
