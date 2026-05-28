package batalha.naval.controlador;

import batalha.naval.cliente.Cliente;
import batalha.naval.swing.Window;
import library.payload.comunicacao.EstadosJogo;
import library.payload.comunicacao.Mensagem;
import library.payload.comunicacao.Validacao;
import library.payload.tabuleiro.Posicao;
import library.payload.tabuleiro.Tabuleiro;

import java.awt.event.KeyListener;

public class LogicaJogo implements Runnable {
    private Window window;
    private Cliente cliente;
    private volatile boolean running = true;

    public LogicaJogo(Window window, Cliente cliente) {
        this.window = window;
        this.cliente = cliente;
    }

    public void stop() {
        running = false;
    }

    public void run() {
        while (running) {
            Object input = cliente.readInput();

            if (input == null) break;

            if (input instanceof EstadosJogo) {
                EstadosJogo estadoJogo = (EstadosJogo) input;
                resolveEstadosJogo(estadoJogo);
                continue;
            }

            if (input instanceof Validacao) {
                Validacao validacao = (Validacao) input;
                window.appendLog(validacao.toString());
                continue;
            }

            if (input instanceof Tabuleiro){
                Tabuleiro tabuleiro = (Tabuleiro) input;
                if (tabuleiro.isTabuleiroTipoTiro()) {
                    window.updateTabuleiroTiros(tabuleiro);

                    tabuleiro.imprime();
                } else {
                    window.updateTabuleiroBarcos(tabuleiro);
                }

                removeKeyListener();
            }

            if(input instanceof Mensagem){
                Mensagem mensagem = (Mensagem) input;
                window.appendLog(mensagem.getMensagem());
                continue;
            }

        }
    }

    private void removeKeyListener(){
        KeyListener[] keyListeners = window.getKeyListeners();
        for(KeyListener keyListener : keyListeners){
            window.removeKeyListener(keyListener);
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
