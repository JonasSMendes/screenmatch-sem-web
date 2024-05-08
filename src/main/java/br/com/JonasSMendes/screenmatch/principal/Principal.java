package br.com.JonasSMendes.screenmatch.principal;

import br.com.JonasSMendes.screenmatch.model.DadosSerie;
import br.com.JonasSMendes.screenmatch.model.DadosTemporada;
import br.com.JonasSMendes.screenmatch.model.Serie;
import br.com.JonasSMendes.screenmatch.repository.SerieRepository;
import br.com.JonasSMendes.screenmatch.service.ConsumoApi;
import br.com.JonasSMendes.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    private ConsumoApi consumo = new ConsumoApi();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=5774020";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private final SerieRepository serieRepository;
    public Principal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }


    public void exibeMenu(){
        var opcoes = -1;

        while (opcoes != 0){

            var menu = """
                    1 - buscar serie
                    2 - buscar episodio
                    3 - listar series buscadas
                    
                    0 - sair
                """;

            System.out.println(menu);

            opcoes = leitura.nextInt();
            leitura.nextLine();

            switch (opcoes){
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscaEpisodiosPorSerie();
                    break;
                case 3:
                    buscarSerieListadas();
                    break;
                case 0:
                    System.out.println("saindo...");
                    break;
                default:
                    System.out.println("opção invalida");
            }
        }
    }

    private void buscarSerieWeb(){
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        serieRepository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie(){
        System.out.println("Digite a serie que deseja");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados( ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterdados(json,DadosSerie.class);

        return dados;
    }

    private void buscaEpisodiosPorSerie(){
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();


        for (int i = 1; i <= dadosSerie.totalTemporadas() ; i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") +"&season="+ i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterdados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);
    }

    private void buscarSerieListadas(){

        List<Serie> series = serieRepository.findAll();
        series.stream()
               .sorted(Comparator.comparing(Serie::getGenero))
               .forEach(System.out::println);
    }


}
