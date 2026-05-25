package batalha.naval.server;

import library.payload.comunicacao.EstadosJogo;

import java.util.Random;

public class BNJogo implements Runnable{
    private String jogoID;
    private BNJogador jogadorA;
    private BNJogador jogadorB;
    private boolean running;

    public BNJogo(BNJogador bnJogador, String jogoID){
        this.jogadorA = bnJogador;
        this.jogadorB = null;

        this.jogoID = jogoID;
        this.running = false;

        System.out.println("A criar jogo JogoID: " + jogoID);
    }

    public synchronized boolean esperaJogador(){
        if(jogadorA == null || jogadorB == null)
            return true;

        return false;
    }

    public synchronized void addJogador(BNJogador bnJogador){
        if(jogadorA == null){
            jogadorA = bnJogador;
            jogadorB.writeInput(EstadosJogo.OPONENTE_CONECTION);
        }
        else{
            jogadorB = bnJogador;
            jogadorA.writeInput(EstadosJogo.OPONENTE_CONECTION);
        }

        System.out.println("GameID: " + jogoID + " Addicionar PlayerID: " + bnJogador.getPlayerID());

    }

    public void run() {
        Random random = new Random();
        boolean jogadorATurno = random.nextBoolean();
        running = true;
        while(running){

        }
    }

    public synchronized void removerJogador(BNJogador bnJogador){
        if(jogadorA == bnJogador){
            jogadorA = null;
            System.out.println("GameID: " + jogoID + " Remover PlayerID: " + bnJogador.getPlayerID());
        }

        if(jogadorB == bnJogador){
            jogadorB = null;
            System.out.println("GameID: " + jogoID + " Remover PlayerID: " + bnJogador.getPlayerID());
        }

        if(jogadorA == null && jogadorB == null){
            running = false;
            System.out.println("GameID: " + jogoID + " Sem Jogadores");
        }
    }

}
