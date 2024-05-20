package br.com.JonasSMendes.screenmatch.service;

import br.com.JonasSMendes.screenmatch.dto.EpisodioDTO;
import br.com.JonasSMendes.screenmatch.dto.SerieDTO;
import br.com.JonasSMendes.screenmatch.model.Categoria;
import br.com.JonasSMendes.screenmatch.model.Serie;
import br.com.JonasSMendes.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;


    public List<SerieDTO>todasSeries(){
        return converteDados(repository.findAll());
    }

    public List<SerieDTO> top5(){
        return converteDados( repository.findTop5ByOrderByAvaliacaoDesc());
    }

    private List<SerieDTO> converteDados(List<Serie> serie){
        return serie
                .stream()
                .map(s -> new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getImg(),s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterLancamento() {
        return converteDados(repository.lancamentosMaisRecentes());
    }

    public SerieDTO obterSerie(Long id) {
        Optional<Serie> serie =  repository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getImg(),s.getSinopse());
        }
        return null;
    }


    public List<EpisodioDTO> serieTemporada(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()){

            return serie.get().getEpisodios()
                            .stream()
                            .map(e -> new EpisodioDTO(e.getTitulo(),e.getNumeroEpisodio(),e.getTemporada()))
                            .collect(Collectors.toList());
        }

        return null;
    }


    public List<EpisodioDTO> pegarTemporada(Long id, Long numero) {
        return repository.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTitulo(),e.getNumeroEpisodio(),e.getTemporada()))
                .collect(Collectors.toList());

    }


    public List<SerieDTO> seriePorGenero(String genero) {
        Categoria categoria = Categoria.fromPortugues(genero);

        return converteDados(repository.findByGenero(categoria));
    }
}
