package org.example;

import batalha.naval.cliente.*;
import library.payload.barco.BarcoBote;
import library.payload.barco.Orientacao;
import library.payload.tabuleiro.*;

public class Main {
    static void main() {
        //Cliente cliente = new Cliente("localhost", 8080);

        Tabuleiro tabuleiro = new Tabuleiro();
        BarcoBote barco = new BarcoBote();

        tabuleiro.adicionarBarco(barco, new Posicao(0, 0), Orientacao.NORTE);
        tabuleiro.adicionarBarco(barco, new Posicao(9, 9), Orientacao.NORTE);

        tabuleiro.imprime();
    }
}
