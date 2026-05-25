package library.payload.tabuleiro;

import java.awt.*;

public enum EstadosTabuleiro {
    MAR(new Color(0, 255, 255)),
    BARCO_BOTE(new Color(225, 168, 80)),
    BARCO_LANCHA(new Color(200, 69, 230)),
    BARCO_SUBMARINO(new Color(179, 50, 69)),
    BARCO_VELEIRO(new Color(74, 156, 75)),
    BARCO_PORTA_AVIOES(new Color(76, 62, 197)),
    ERROU(new Color(202, 56, 56)),
    ACERTOU(new Color(57, 151, 27));

    private final Color cor;
    private EstadosTabuleiro(Color color){
        cor = color;
    }

    public Color getColor(){
        return cor;
    }
}
