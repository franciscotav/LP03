package batalha.naval.server;

import library.payload.comunicacao.*;

import java.io.IOException;
import java.io.ObjectInputStream;
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
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            EstadosMenu estadosMenu = (EstadosMenu) objectInputStream.readObject();
            opcoesIniciais(estadosMenu);

        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void opcoesIniciais(EstadosMenu estado){
        switch(estado){
            case NOVO_JOGO:
                BNJogo bnJogoDisponivel = servidor.getBNJogoDisponivel();

                if(bnJogoDisponivel == null){
                    criarNovoJogo();
                    System.out.println("Novo Jogo criado");
                }else{
                    bnJogoDisponivel.addJogador(this);
                    System.out.println("Jogador adicionado a Jogo");
                }

                break;
            case CARREGAR_JOGO:
                break;
            case QUIT:
                break;
            default:

        }
    }

    private void criarNovoJogo(){
        BNJogo bnJogo = new BNJogo(this, servidor.criarJogoID());
        servidor.addBNJogo(bnJogo);

        Thread jogoThread = new Thread(bnJogo);
        jogoThread.start();

        System.out.println("New Game");
    }
}
