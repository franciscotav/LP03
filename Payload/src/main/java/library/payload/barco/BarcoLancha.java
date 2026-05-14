package library.payload.barco;

import library.payload.tabuleiro.EstadosTabuleiro;

public class BarcoLancha extends Barco{
    public BarcoLancha(){
        super(EstadosTabuleiro.BARCO_LANCHA, 2);
    }
}
