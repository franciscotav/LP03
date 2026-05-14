package library.payload.barco;

import library.payload.tabuleiro.EstadosTabuleiro;

public abstract class Barco {
    private EstadosTabuleiro tipo;
    private int comprimento;


    public Barco(EstadosTabuleiro tipo, int comprimento){
        this.tipo=tipo;
        this.comprimento=comprimento;
    }


    public int getComprimento(){
        return this.comprimento;
    }

    public EstadosTabuleiro getTipo(){
        return this.tipo;
    }
}
