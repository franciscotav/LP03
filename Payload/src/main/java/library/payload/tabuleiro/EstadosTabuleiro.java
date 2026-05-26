package library.payload.tabuleiro;

import java.awt.*;

public enum EstadosTabuleiro {
    MAR(new Color(0, 255, 255)),
    BARCO_BOTE(new Color(48, 168, 9)),
    BARCO_LANCHA(new Color(200, 69, 230)),
    BARCO_SUBMARINO(new Color(60, 21, 124)),
    BARCO_VELEIRO(new Color(234, 226, 2)),
    BARCO_PORTA_AVIOES(new Color(166, 52, 85)),
    ERROU(new Color(255, 255, 255)),
    ACERTOU(new Color(0, 0, 0)),
    //acertou no nosso barco
    DANO(new Color(0, 0, 0));

    private final Color cor;
    private EstadosTabuleiro(Color color){
        cor = color;
    }

    public Color getColor(){
        return cor;
    }
}
