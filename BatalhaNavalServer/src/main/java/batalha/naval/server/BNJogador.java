package batalha.naval.server;

import library.payload.comunicacao.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BNJogador implements Runnable {
    private Servidor servidor;
    private Socket socket;
    boolean update;

    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    public BNJogador(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void run(){
        while(true){
            writeInput(Validacao.OK);
            readInput();
        }
    }

    public void readInput(){
        try{
            Object input = objectInputStream.readObject();

            if(input instanceof EstadosMenu){
                opcoesIniciais((EstadosMenu) input);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public void writeInput(Validacao validacao){
        try{
            objectOutputStream.writeObject(validacao);
        } catch (IOException e) {
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
