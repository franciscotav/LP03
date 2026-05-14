package library.payload.barco;

import library.payload.tabuleiro.EstadosTabuleiro;

public class BarcoBote extends Barco{
    public BarcoBote(){
        super(EstadosTabuleiro.BARCO_BOTE, 1);
    }
}
