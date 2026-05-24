package batalha.naval.controlador;

import batalha.naval.cliente.Cliente;
import batalha.naval.swing.Window;
import library.payload.comunicacao.EstadosJogo;
import library.payload.comunicacao.Validacao;

public class LogicaJogo implements Runnable {
    private Window window;
    private Cliente cliente;

    public LogicaJogo(Window window, Cliente cliente) {
        this.window = window;
        this.cliente = cliente;
    }

    public void run() {
        while (true) {
            Object input = cliente.readInput();

            if (input instanceof EstadosJogo) {
                EstadosJogo estadoJogo = (EstadosJogo) input;
                resolveEstadosJogo(estadoJogo);
            }

            if (input instanceof Validacao) {
                Validacao validacao = (Validacao) input;
                window.appendLog(validacao.toString());
            }
        }
    }

    private void resolveEstadosJogo(EstadosJogo estadoJogo){
        switch (estadoJogo){
            case OPONENTE_CONECTION:
                window.appendLog("Oponente conectado");
                break;
            case OPONENTE_DISCONETION:
                window.appendLog("Oponente desconectado");
                break;
            case TURNO:
                window.appendLog("É o teu turno");
                break;
            case OPONENTE_TURNO:
                window.appendLog("A espera do input do oponente");
                break;
        }
    }
}
