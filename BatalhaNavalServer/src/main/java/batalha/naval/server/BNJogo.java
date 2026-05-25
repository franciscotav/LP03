package batalha.naval.server;

import library.payload.comunicacao.EstadosJogo;
import library.payload.tabuleiro.Tabuleiro;

import java.util.Random;

public class BNJogo implements Runnable{
    private String jogoID;

    private BNJogador jogadorA;
    private BNJogador jogadorB;

    private Tabuleiro jogadorATabuleiroBarcos;
    private Tabuleiro jogadorATabuleiroTiros;
    private Tabuleiro jogadorBTabuleiroBarcos;
    private Tabuleiro jogadorBTabuleiroTiros;

    private boolean running;

    private boolean jogadorAturno;
    private boolean jogadorBturno;

    public BNJogo(BNJogador bnJogador, String jogoID){
        this.jogadorA = bnJogador;
        this.jogadorB = null;

        this.jogoID = jogoID;
        this.running = false;

        this.jogadorATabuleiroBarcos = null;
        this.jogadorATabuleiroTiros = new Tabuleiro();
        this.jogadorBTabuleiroBarcos = null;
        this.jogadorBTabuleiroTiros = new Tabuleiro();

        this.jogadorAturno = false;
        this.jogadorBturno = false;

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

    public void addTabuleiro(BNJogador bnJogador, Tabuleiro tabuleiroBarcos){
        if(bnJogador == jogadorA){
            jogadorATabuleiroBarcos = tabuleiroBarcos;
            jogadorATabuleiroBarcos.imprime();
        }

        if(bnJogador == jogadorB){
            jogadorBTabuleiroBarcos = tabuleiroBarcos;
            jogadorBTabuleiroBarcos.imprime();
        }
    }

    public void run() {
        aleatorioJogador();
        running = true;
        while(running){
            if(condicaoesJogo()){

            }
        }
    }

    private void aleatorioJogador(){
        Random random = new Random();
        if(random.nextBoolean()){
            jogadorAturno = true;
            jogadorBturno = false;
        }else{
            jogadorAturno = false;
            jogadorBturno = true;
        }
    }

    public boolean condicaoesJogo(){
        if(jogadorA == null || jogadorB == null)
            return false;

        return true;
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
