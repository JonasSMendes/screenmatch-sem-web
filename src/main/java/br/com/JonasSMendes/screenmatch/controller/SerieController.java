package br.com.JonasSMendes.screenmatch.controller;

import br.com.JonasSMendes.screenmatch.dto.EpisodioDTO;
import br.com.JonasSMendes.screenmatch.dto.SerieDTO;
import br.com.JonasSMendes.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")  // Permitir todas as origens
@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO> obterSeries(){
       return service.todasSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> top5(){
      return service.top5();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos(){
        return service.obterLancamento();
    }

    @GetMapping("/{id}")
    public SerieDTO obterSerie(@PathVariable Long id){
        return service.obterSerie(id);
    }



    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> serieTemporadas (@PathVariable Long id){
        return service.serieTemporada(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> numeroTemporada(@PathVariable Long id, @PathVariable Long numero){
        return service.pegarTemporada(id, numero);
    }

    @GetMapping("/categoria/{genero}")
    public List<SerieDTO> seriesPorGenero(@PathVariable String genero){
        return service.seriePorGenero(genero);
    }

}