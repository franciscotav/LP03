package batalha.naval.server;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class Servidor{
    private LinkedList<BNJogo> bnJogos;
    private BNServerSocket bnServerSocket;
    private SalaEspera salaEspera;

    public Servidor(int porta){
        try{
            bnJogos = new LinkedList<>();
            bnServerSocket = new BNServerSocket(porta);
            salaEspera = new SalaEspera();
            System.out.println("ServerSocket na porta: " + porta);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void init(){
        Thread listeningThread = new Thread(bnServerSocket);
        listeningThread.start();

        SalaEspera salaEspera = new SalaEspera();
        Thread salaThread = new Thread(salaEspera);
        salaThread.start();

        while(true){
            if(!bnServerSocket.clients().isEmpty()){
                Socket socket = bnServerSocket.clients().removeFirst();

                BNJogador bnJogador = new BNJogador(socket, this);
                Thread jogadorThread = new Thread(bnJogador);
                jogadorThread.start();

                salaEspera.addJogador(bnJogador);
            }
        }
    }

    public LinkedList<BNJogo> getBNJogos(){
        return bnJogos;
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
}