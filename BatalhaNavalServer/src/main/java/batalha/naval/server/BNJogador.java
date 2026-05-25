package batalha.naval.server;

import library.payload.comunicacao.*;
import library.payload.tabuleiro.Posicao;
import library.payload.tabuleiro.Tabuleiro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class BNJogador implements Runnable {
    private Socket socket;
    private Servidor servidor;
    private String playerId;
    private boolean running;
    private BNJogo bnJogo;

    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    public BNJogador(Socket socket, Servidor servidor, String playerID) {
        this.socket = socket;
        this.servidor = servidor;
        this.playerId = playerID;
        this.running = false;
        this.bnJogo = null;

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("A criar jogador PlayerID: " + playerID);
    }

    public void run(){
        running = true;
        while(running){
            Object resposta = readInput();

            //caso o cliente dé desconect
            if(!running) break;

            writeInput(resposta);
        }

        bnJogo.removerJogador(this);
    }


    public Object readInput(){
        try{
            Object input = objectInputStream.readObject();

            if(input instanceof EstadosMenu){
                opcoesIniciais((EstadosMenu) input);
                return Validacao.WAITING_INPUT;
            }

            if(input instanceof Posicao){
                Posicao tiro = (Posicao) input;
                System.out.println("Recebi tiro(x,y): " + tiro.getX() + ", " + tiro.getY());
                if(bnJogo.turno(this)){
                    Mensagem mensagem = bnJogo.tiroTabuleiro(this, tiro);
                    writeInput(mensagem);
                    //Temos que desabilitar os tiros sem outro jogador!
                }

                return Validacao.OK;
            }


            if(input instanceof Tabuleiro){
                Tabuleiro tabuleiroBarcos = (Tabuleiro) input;
                bnJogo.addTabuleiro(this, tabuleiroBarcos);
                return Validacao.OK;
            }

        }catch(SocketException e){
            System.out.println("Ligação perdida com jogador PlayerID: " + playerId);
            running = false;
        }catch (IOException e) {
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        return Validacao.ERROR;
    }

    public void writeInput(Object output){
        try{
            objectOutputStream.writeObject(output);
        }catch(SocketException e){
            System.out.println("Ligação perdida com jogador PlayerID: " + playerId);
            running = false;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void opcoesIniciais(EstadosMenu estado){
        switch(estado){
            case NOVO_JOGO:
                BNJogo bnJogoDisponivel = servidor.getBNJogoDisponivel();

                if(bnJogoDisponivel == null){
                    criarNovoJogo();
                }else{
                    this.bnJogo = bnJogoDisponivel;
                    bnJogoDisponivel.addJogador(this);
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
        this.bnJogo = bnJogo;

        Thread jogoThread = new Thread(bnJogo);
        jogoThread.start();
    }

    public String getPlayerID(){
        return playerId;
    }
}
