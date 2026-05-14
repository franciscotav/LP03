package library.payload.barco;

import library.payload.tabuleiro.EstadosTabuleiro;

public class BarcoVeleiro extends Barco{
    public BarcoVeleiro(){
        super(EstadosTabuleiro.BARCO_VELEIRO, 4);
    }
}
