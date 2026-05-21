package batalha.naval.server;

import java.util.LinkedList;

public class SalaEspera implements Runnable {
    private LinkedList<BNJogador> jogadores;
    public SalaEspera()
    {
        jogadores = new LinkedList<>();
    }
    public void run(){
        while(true){
            if(!jogadores.isEmpty())
            {
                BNJogador bnJogador = jogadores.removeFirst();
                Thread jogador = new Thread(bnJogador);
                jogador.start();
            }
        }

    }

    public void addJogador(BNJogador jogador)
    {
        jogadores.add(jogador);

    }
}
