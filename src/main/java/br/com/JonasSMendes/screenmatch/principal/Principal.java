package br.com.JonasSMendes.screenmatch.principal;

import br.com.JonasSMendes.screenmatch.model.DadosEpisodios;
import br.com.JonasSMendes.screenmatch.model.DadosSerie;
import br.com.JonasSMendes.screenmatch.model.DadosTemporada;
import br.com.JonasSMendes.screenmatch.model.Episodio;
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

    public void exibeMenu(){
        System.out.println("Digite o nome da serie para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados( ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterdados(json,DadosSerie.class);

        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

//
		for (int i = 1; i <= dados.totalTemporadas() ; i++) {
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season="+ i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterdados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
//
//      temporadas.forEach(System.out::println);

//      temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodios> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                        .collect(Collectors.toList());

//        System.out.println("\n top 10 episodios");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
//                .limit(10)
//                .map(e -> e.titulo().toUpperCase())
//                .forEach(System.out::println);

//        List<String> nomes = Arrays.asList("Joj", "viv", "lala");
//
//        nomes.stream()
//                .sorted()
//                .limit(2)
//                .filter(j -> j.startsWith("J"))
//                .map(j -> j.toUpperCase())
//                .forEach(System.out::println);

        //        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodios> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        List<Episodio> episodio = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodio.forEach(System.out::println);

//        System.out.println("Digite o nome do episodio");
//        var trechoTitulo = leitura.nextLine();
//        Optional<Episodio> episodioBuscado = episodio.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//
//        if (episodioBuscado.isPresent()){
//            System.out.println("episodio encontrado");
//            System.out.println("temporada: " + episodioBuscado.get().getTemporada());
//        }else {
//            System.out.println("esse episodio não existe");
//        }

//        System.out.println("A partir de que ano você deseja ver os episodios? ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodio.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada " + e.getTemporada() +
//                               " Episodio: " + e.getTitulo() +
//                                " Data lançamento: " + e.getDataLancamento().format(formatador)
//                ));

        Map<Integer ,Double> avaliacaoTemporada = episodio.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacaoTemporada);

        DoubleSummaryStatistics est = episodio.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("media: " + est.getAverage());
        System.out.println("melhor episodio : " + est.getMax());
        System.out.println("pior episodio: " + est.getMin());
        System.out.println("quantidade de episodios avaliados: " + est.getCount());

    }

}
