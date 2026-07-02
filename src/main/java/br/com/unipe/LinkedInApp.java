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