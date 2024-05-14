package br.com.JonasSMendes.screenmatch.principal;

import br.com.JonasSMendes.screenmatch.model.*;
import br.com.JonasSMendes.screenmatch.repository.EpisodiosRepository;
import br.com.JonasSMendes.screenmatch.repository.SerieRepository;
import br.com.JonasSMendes.screenmatch.service.ConsumoApi;
import br.com.JonasSMendes.screenmatch.service.ConverteDados;


import javax.sound.midi.Soundbank;
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
                    1 - Buscar serie
                    2 - Buscar episodio
                    3 - Listar series buscadas
                    4 - Buscar serie por titulo
                    5 - Buscar serie pelo ator
                    6 - Top 5 series mais bem avaliadas
                    7 - Buscar por genero
                    8 - Buscar total de temporadas com avaliação
                    
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
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5();
                    break;
                case 7:
                    buscaPorGenero();
                    break;
                case 8:
                    buscarPeloTotalTemporada();
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

    private void buscarSeriePorTitulo() {
        System.out.println("escolha uma serie pelo nome");
        var nomeSerie = leitura.nextLine();

        Optional<List<Serie>> serieBuscada = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()){
            System.out.println("Dados da serie" + serieBuscada.get());
        }else {
            System.out.println("serie não encontrada");
        }

    }

    private void buscarSeriePorAtor(){
        System.out.println("procure um ator que trabalhou na serie que procura");
        var nomeSerie = leitura.nextLine();

        System.out.println("avaliação a apertir de que valor? ");
        var notaDaSerie = leitura.nextDouble();

        List<Serie> atorBuscado = serieRepository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeSerie, notaDaSerie);


        if (atorBuscado.size() > 0){
            System.out.println("serie onde o ator " + nomeSerie + " trabalhou");
            atorBuscado.forEach(a ->
                    System.out.println(a.getTitulo() + " avaliação: " + a.getAvaliacao()));

        }else {
            System.out.println("serie com esse ator não encontrada");
        }

    }

    private void buscarTop5(){

        List<Serie> buscaTop = serieRepository.findTop5ByOrderByAvaliacaoDesc();

        buscaTop.forEach(s ->
                System.out.println("top 5 series: " + s.getTitulo() + " avaliação: " + s.getAvaliacao()));

    }

    private void buscaPorGenero(){
        System.out.println("procure um ator que trabalhou na serie que procura");
        var genero = leitura.nextLine();

        Categoria categoria = Categoria.fromPortugues(genero);

        List<Serie> buscaPorgenero = serieRepository.findByGenero(categoria);

        buscaPorgenero.forEach(c ->
                System.out.println("lista do genero " + genero + ": " + c.getTitulo()+ " Genero: " + c.getGenero() ));
    }

    private void buscarPeloTotalTemporada(){
        System.out.println("Quantas temporadas maximas precisa ter?");
        var totalTemporada = leitura.nextInt();
        leitura.nextLine();

        System.out.println("quanto de avaliação a serie precisa ter?");
        var avaliacaoSerie = leitura.nextDouble();


        List<Serie> buscaTotalTemporada =
                serieRepository.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(totalTemporada, avaliacaoSerie);

        buscaTotalTemporada.forEach(s ->
                System.out.println("series filtradas: "  + " temporadas: " + totalTemporada + " avaliação: " + avaliacaoSerie + " Titulo: " + s.getTitulo() + " avaliação: " + s.getAvaliacao() + "temporadas" + s.getTotalTemporadas()));

    }


}

