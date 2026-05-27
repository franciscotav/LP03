package batalha.naval.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingDeque;

public class Servidor{
    private static int numeroJogos;
    private static int numeroJogadores;
    private LinkedBlockingDeque<BNJogo> bnJogos;
    private BNServerSocket bnServerSocket;

    public Servidor(int porta){
        try{
            numeroJogos = 0;
            numeroJogadores = 0;
            bnJogos = new LinkedBlockingDeque<>();

            bnServerSocket = new BNServerSocket(porta);
            Thread listeningThread = new Thread(bnServerSocket);
            listeningThread.start();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            try{
                Socket socket = bnServerSocket.clients().take();
                BNJogador bnJogador = new BNJogador(socket, this, criarPlayerID());

                Thread jogadorThread = new Thread(bnJogador);
                jogadorThread.start();

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public LinkedBlockingDeque<BNJogo> getBNJogos(){
        return bnJogos;
    }

    public BNJogo getBNJogo(String idJogo){
        for(BNJogo bnJogo: bnJogos){
            if(bnJogo.getJogoId().equals(idJogo)){
                return bnJogo;
            }
        }
        return null;
    }

    public void addBNJogo(BNJogo bnJogo){
        bnJogos.add(bnJogo);
    }

    public BNJogo getBNJogoDisponivel(){
        for(BNJogo bnJogo : bnJogos){
            if(bnJogo.esperaJogador()){
                return bnJogo;
            }
        }

        return null;
    }

    public synchronized String criarJogoID(){
        numeroJogos++;
        return "GAME%d".formatted(numeroJogos);
    }

    public synchronized String criarPlayerID(){
        numeroJogadores++;
        return "PLAYER%d".formatted(numeroJogadores);
    }
}