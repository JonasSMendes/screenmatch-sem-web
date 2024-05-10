package br.com.JonasSMendes.screenmatch.principal;

import br.com.JonasSMendes.screenmatch.model.DadosSerie;
import br.com.JonasSMendes.screenmatch.model.DadosTemporada;
import br.com.JonasSMendes.screenmatch.model.Episodio;
import br.com.JonasSMendes.screenmatch.model.Serie;
import br.com.JonasSMendes.screenmatch.repository.EpisodiosRepository;
import br.com.JonasSMendes.screenmatch.repository.SerieRepository;
import br.com.JonasSMendes.screenmatch.service.ConsumoApi;
import br.com.JonasSMendes.screenmatch.service.ConverteDados;


import java.util.*;
import java.util.stream.Collectors;


public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    private ConsumoApi consumo = new ConsumoApi();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=5774020";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private List<Serie> series = new ArrayList<>();

    private final SerieRepository serieRepository;
    private final EpisodiosRepository episodiosRepository;

    public Principal(SerieRepository serieRepository, EpisodiosRepository episodiosRepository) {
        this.serieRepository = serieRepository;
        this.episodiosRepository = episodiosRepository;
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
        buscarSerieListadas();
        System.out.println("escolha uma serie pelo nome");
        var nomeSerie = leitura.nextLine();

      Optional<Serie> serie =  series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();


      if (serie.isPresent()){

          var serieEncontrada = serie.get();
          List<DadosTemporada> temporadas = new ArrayList<>();

          for (int i = 1; i <= serieEncontrada.getTotalTemporadas() ; i++) {
              var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") +"&season="+ i + API_KEY);
              DadosTemporada dadosTemporada = conversor.obterdados(json, DadosTemporada.class);
              temporadas.add(dadosTemporada);
          }
          temporadas.forEach(System.out::println);

          List<Episodio> episodios = temporadas.stream()
                  .flatMap(d -> d.episodios().stream()
                          .map(e -> new Episodio(d.numero(), e)))
                  .collect(Collectors.toList());

          serieEncontrada.setEpisodios(episodios);
          serieRepository.save(serieEncontrada);


      }else {
          System.out.println("serie não encontrada");
      }

    }

    private void buscarSerieListadas(){
        series = serieRepository.findAll();
        series.stream()
               .sorted(Comparator.comparing(Serie::getGenero))
               .forEach(System.out::println);
    }


}
