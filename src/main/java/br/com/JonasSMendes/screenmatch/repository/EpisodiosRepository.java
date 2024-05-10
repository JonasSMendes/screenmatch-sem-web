package br.com.JonasSMendes.screenmatch.repository;

import br.com.JonasSMendes.screenmatch.model.Episodio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodiosRepository extends JpaRepository<Episodio, Long> {
}
