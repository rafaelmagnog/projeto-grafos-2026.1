package br.com.unipe;

import java.util.List;

public class LinkedInApp {
    public static void main(String[] args) {
        Grafo grafo = new Grafo(false, true); // não-direcionado e ponderado

        // Rede principal
        grafo.adicionaVertices("Ana", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda");
        // Grupo isolado 1
        grafo.adicionaVertices("Gabriel", "Hugo");
        // Grupo isolado 2
        grafo.adicionaVertices("Igor", "Juliana");

        grafo.addAresta("Ana", "Bruno", 1);
        grafo.addAresta("Ana", "Carlos", 2);
        grafo.addAresta("Ana", "Daniela", 8);
        grafo.addAresta("Bruno", "Eduardo", 1);
        grafo.addAresta("Carlos", "Eduardo", 1);
        grafo.addAresta("Daniela", "Fernanda", 5);
        grafo.addAresta("Eduardo", "Fernanda", 1);
        grafo.addAresta("Gabriel", "Hugo", 1);
        grafo.addAresta("Igor", "Juliana", 1);

		LinkedInAnalyzer analyzer = new LinkedInAnalyzer(grafo);
		
		System.out.println("===== Missão 2: Sugestão de Conexões (amigos de 2º grau) =====");
		List<LinkedInAnalyzer.SugestaoDeConexao> sugestoesParaAna = analyzer.sugereConexoes("Ana");
		System.out.println("Sugestões para Ana: " + sugestoesParaAna);
		
		System.out.println("\n===== Missão 3: Grau de Separação =====");
		int grauAnaFernanda = analyzer.calculaGrauDeSeparacao("Ana", "Fernanda");
		System.out.println("Grau de separação entre Ana e Fernanda: " + grauAnaFernanda + " salto(s)");
		
		int grauAnaIgor = analyzer.calculaGrauDeSeparacao("Ana", "Igor");
		System.out.println("Grau de separação entre Ana e Igor (grupos isolados, sem conexão): " + grauAnaIgor);
		
		System.out.println("\n===== Missão 4: Rota e Custo de Maior Afinidade (Dijkstra) =====");
		Grafo.ResultadoCaminhoPonderado rotaAnaFernanda = analyzer.encontraRotaDeMaiorAfinidade("Ana", "Fernanda");
		System.out.println("Rota de maior afinidade de Ana até Fernanda: " + rotaAnaFernanda.caminho());
		System.out.println("Custo total da rota: " + rotaAnaFernanda.custoTotal());
		System.out.println("""
                Comparação:
                - Rota com MENOS saltos: Ana -> Daniela -> Fernanda (2 saltos), custo = 13.
                - Rota de MAIOR afinidade (Dijkstra): Ana -> Bruno -> Eduardo -> Fernanda (3 saltos), custo = 3.
                Ou seja, o caminho mais curto em número de conexões não é o de maior afinidade!""");
		
		System.out.println("\n===== Missão 5: Mapear Grupos Isolados (Sub-redes) =====");
		List<List<String>> gruposIsolados = analyzer.mapeiaGruposIsolados();
		for (List<String> grupo : gruposIsolados) {
			System.out.println("Grupo: " + grupo);
		}
	}
}