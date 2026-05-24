package library.payload.comunicacao;

import java.io.Serializable;

public enum EstadosMenu implements Serializable {
    NOVO_JOGO, CARREGAR_JOGO, PAUSA, QUIT;
}
