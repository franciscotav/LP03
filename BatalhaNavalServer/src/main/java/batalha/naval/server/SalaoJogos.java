package batalha.naval.server;

import java.util.LinkedList;
import library.payload.*;
import library.payload.comunicacao.EstadosMenu;

public class SalaoJogos implements Runnable {
    private LinkedList<BNJogo> bnJogos;
    private LinkedList<BNJogador> bnJogadores;

    public SalaoJogos(){
        bnJogos = new LinkedList<>();
        bnJogadores = new LinkedList<>();
    }

    @Override
    public void run() {
        while(true){
            if(!bnJogadores.isEmpty()){
                BNJogador jogadorAtivo = bnJogadores.removeFirst();
                jogadorAtivo.readInput();


            }
        }
    }

    public void addJogador(BNJogador bnJogador){
        bnJogadores.add(bnJogador);
    }



    private BNJogo procurarJogo(){
        for(BNJogo jogo : bnJogos){
            if(jogo.esperaJogador())
                return jogo;
        }

        return null;
    }

    private void criaJogo(){
    }
}
