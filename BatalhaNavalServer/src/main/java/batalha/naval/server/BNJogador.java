package batalha.naval.server;

import library.payload.comunicacao.*;
import library.payload.tabuleiro.Posicao;
import library.payload.tabuleiro.Tabuleiro;

import java.io.*;
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
            readInput();
        }

        bnJogo.removerJogador(this);
    }


    public void readInput(){
        try{
            Object input = objectInputStream.readObject();

            if(input instanceof EstadosMenu){
                opcoesIniciais((EstadosMenu) input);
                return;
            }

            if(input instanceof Posicao){
                if(!bnJogo.condicaoesJogo()){
                    System.out.println("PlayerID: " + playerId + " Tiro indisponivel");
                    writeInput(new Mensagem("(Jogo não compre requesitos minimos)"));
                    writeInput(Validacao.INVALID_INPUT);
                    return;
                }

                Posicao tiro = (Posicao) input;
                System.out.println("PlayerID: " + playerId +  " Tiro:(" + tiro.getX() + ", " + tiro.getY() + ")");
                if(bnJogo.turno(this)){
                    bnJogo.tiroTabuleiro(this, tiro);
                }else{
                    writeInput(new Mensagem("(Turno do oponente)"));
                    writeInput(Validacao.INVALID_INPUT);
                }

                return;
            }

            if(input instanceof Tabuleiro){
                Tabuleiro tabuleiroBarcos = (Tabuleiro) input;
                bnJogo.addTabuleiro(this, tabuleiroBarcos);
                return;
            }

        }catch(SocketException e){
            System.out.println("Ligação perdida com jogador PlayerID: " + playerId);
            running = false;
        }catch (IOException e) {
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }


        writeInput(Validacao.ERROR);
    }

    public void writeInput(Object output){
        try{
            objectOutputStream.reset();
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
                    writeInput(Validacao.WAITING_INPUT);
                }else{
                    this.bnJogo = bnJogoDisponivel;
                    bnJogoDisponivel.addJogador(this);
                    writeInput(Validacao.WAITING_INPUT);
                }

                break;
            case CARREGAR_JOGO:
                break;
            case GUARDAR:
                guardarJogo();
                break;
            case QUIT:
                break;
            default:

        }
    }

    private void guardarJogo(){
        bnJogo.guardarJogo();
    }

    private void criarNovoJogo(){
        BNJogo bnJogo = new BNJogo(this, servidor.criarJogoID());
        servidor.addBNJogo(bnJogo);
        this.bnJogo = bnJogo;

    }

    public String getPlayerID(){
        return playerId;
    }
}
