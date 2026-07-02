package br.com.unipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class LinkedInAnalyzer {

    private final Grafo grafo;

    public LinkedInAnalyzer(Grafo grafo) {
        this.grafo = grafo;
    }

    /**
     * Estrutura de retorno da Missão 2: nome da pessoa sugerida + quantidade de
     * amigos em comum com o usuário que pediu a sugestão.
     */
    public record SugestaoDeConexao(String nome, int amigosEmComum) {
    }

    /**
     * Missão 2 — Sugestão de Conexões (amigos de 2º grau).
     * Para cada amigo direto da pessoa, olhamos os amigos DELE. Quem aparecer
     * assim, sem já ser amigo direto e sem ser a própria pessoa, é candidato a
     * sugestão. Contamos quantas vezes cada candidato apareceu (= quantos amigos
     * em comum ele tem com a pessoa) e ordenamos do maior para o menor.
     */
    public List<SugestaoDeConexao> sugereConexoes(String nomeDaPessoa) {
        Vertice pessoa = grafo.encontraVertice(nomeDaPessoa).orElseThrow(
                () -> new IllegalArgumentException("Vertice " + nomeDaPessoa + " não encontrado."));

        List<Vertice> amigosDiretos = pessoa.getAdjacencias();
        Map<Vertice, Integer> contagemAmigosEmComum = new HashMap<>();

        for (Vertice amigo : amigosDiretos) {
            for (Vertice amigoDoAmigo : amigo.getAdjacencias()) {
                boolean ehAPropriaPessoa = amigoDoAmigo.equals(pessoa);
                boolean jaEhAmigoDireto = amigosDiretos.contains(amigoDoAmigo);

                if (ehAPropriaPessoa || jaEhAmigoDireto) {
                    continue;
                }

                contagemAmigosEmComum.merge(amigoDoAmigo, 1, Integer::sum);
            }
        }

        List<SugestaoDeConexao> sugestoes = new ArrayList<>();
        for (Map.Entry<Vertice, Integer> entrada : contagemAmigosEmComum.entrySet()) {
            sugestoes.add(new SugestaoDeConexao(entrada.getKey().getNome(), entrada.getValue()));
        }

        sugestoes.sort((s1, s2) -> s2.amigosEmComum() - s1.amigosEmComum());

        return sugestoes;
    }

    /**
     * Missão 3 — Grau de Separação.
     * Busca em largura (BFS) IGNORANDO os pesos: cada aresta percorrida conta
     * como 1 salto, não importa a intensidade da conexão. Retorna -1 se as
     * pessoas não existirem ou não estiverem no mesmo grupo conectado.
     */
    public int calculaGrauDeSeparacao(String nomeOrigem, String nomeDestino) {
        Optional<Vertice> origemOpt = grafo.encontraVertice(nomeOrigem);
        Optional<Vertice> destinoOpt = grafo.encontraVertice(nomeDestino);

        if (origemOpt.isEmpty() || destinoOpt.isEmpty()) {
            return -1;
        }

        Vertice origem = origemOpt.get();
        Vertice destino = destinoOpt.get();

        if (origem.equals(destino)) {
            return 0;
        }

        Queue<Vertice> filaDeBusca = new LinkedList<>();
        Map<Vertice, Integer> distanciaEmSaltos = new HashMap<>();

        filaDeBusca.add(origem);
        distanciaEmSaltos.put(origem, 0);

        while (!filaDeBusca.isEmpty()) {
            Vertice atual = filaDeBusca.poll();

            for (Vertice vizinho : atual.getAdjacencias()) {
                if (distanciaEmSaltos.containsKey(vizinho)) {
                    continue; // já visitado
                }

                int distanciaDoVizinho = distanciaEmSaltos.get(atual) + 1;
                distanciaEmSaltos.put(vizinho, distanciaDoVizinho);

                if (vizinho.equals(destino)) {
                    return distanciaDoVizinho;
                }

                filaDeBusca.add(vizinho);
            }
        }

        return -1; // destino não é alcançável a partir da origem
    }

    /**
     * Missão 4 — Rota e Custo de Maior Afinidade.
     * Delega diretamente para o Dijkstra já implementado em Grafo, evitando
     * duplicar a lógica de menor caminho ponderado aqui.
     */
    public Grafo.ResultadoCaminhoPonderado encontraRotaDeMaiorAfinidade(String nomeOrigem, String nomeDestino) {
        return grafo.encontraRotaDeMaiorAfinidade(nomeOrigem, nomeDestino);
    }

    /**
     * Missão 5 — Mapear Grupos Isolados (componentes conexos).
     * Para cada vértice ainda não agrupado, usamos o dfsIterativo já existente em
     * Grafo (chamado com destino = null) para pegar TODO o grupo conectado a ele
     * de uma vez. Marcamos esse grupo inteiro como "já visto" e seguimos para o
     * próximo vértice não agrupado, até cobrir a rede inteira.
     */
    public List<List<String>> mapeiaGruposIsolados() {
        List<List<String>> grupos = new ArrayList<>();
        Set<String> nomesJaAgrupados = new HashSet<>();

        for (Vertice vertice : grafo.getVertices()) {
            String nome = vertice.getNome();

            if (nomesJaAgrupados.contains(nome)) {
                continue;
            }

            List<String> grupoAtual = grafo.dfsIterativo(nome, null);
            grupos.add(grupoAtual);
            nomesJaAgrupados.addAll(grupoAtual);
        }

        return grupos;
    }
}