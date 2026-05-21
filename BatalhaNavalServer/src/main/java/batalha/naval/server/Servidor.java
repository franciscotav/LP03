package batalha.naval.server;

import java.io.*;
import java.net.*;

public class Servidor{
    private BNServerSocket bnServerSocket;

    public Servidor(int porta){
        try{
            bnServerSocket = new BNServerSocket(porta);
            System.out.println("ServerSocket na porta: " + porta);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void init(){
        Thread listeningThread = new Thread(bnServerSocket);
        listeningThread.start();

        while(true){
            if(bnServerSocket.clients().size() >= 1){
                Socket jogador = bnServerSocket.clients().getFirst();
                bnServerSocket.clients().removeFirst();

                BNJogador bnJogador = new BNJogador(jogador);
                Thread jogadorThread = new Thread(bnJogador);
                jogadorThread.start();
            }
        }
    }
}