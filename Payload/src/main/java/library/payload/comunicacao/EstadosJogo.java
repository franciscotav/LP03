package library.payload.comunicacao;

import java.io.Serializable;

public enum EstadosJogo implements Serializable {
    OPONENTE_CONECTION, OPONENTE_DISCONETION, TURNO, OPONENTE_TURNO;
}
