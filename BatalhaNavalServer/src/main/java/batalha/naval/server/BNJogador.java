package batalha.naval.server;

import library.payload.comunicacao.*;
import library.payload.tabuleiro.EstadosTabuleiro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BNJogador implements Runnable {
    private Servidor servidor;
    private Socket socket;

    public BNJogador(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;
    }

    public void run(){
        while(true){
            readInput();
        }
    }

    public void readInput(){
        try{
            //ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            EstadosMenu estadosMenu = (EstadosMenu) objectInputStream.readObject();
            opcoes(estadosMenu);



        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void opcoes(EstadosMenu estado){
        switch(estado){
            case NOVO_JOGO:
                BNJogo bnJogoDisponivel = servidor.getBNJogoDisponivel();

                if(bnJogoDisponivel == null){
                    BNJogo bnJogo = new BNJogo(this);
                    Thread jogoThread = new Thread(bnJogo);
                    jogoThread.start();

                    servidor.addBNJogo(bnJogo);
                }else{
                    bnJogoDisponivel.addJogador(this);
                }

                break;
            case CARREGAR_JOGO:
                break;
            case QUIT:
                break;

        }
    }
}
