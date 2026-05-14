package library.payload.barco;

import library.payload.tabuleiro.EstadosTabuleiro;

public class BarcoSubmarino extends Barco{
    public BarcoSubmarino(){
        super(EstadosTabuleiro.BARCO_SUBMARINO, 3);
    }
}
