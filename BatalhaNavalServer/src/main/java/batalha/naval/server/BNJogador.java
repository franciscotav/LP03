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

    private String idSessao;
    private boolean desconetado;
    private long tempoDesconetado;

    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    public BNJogador(Socket socket, Servidor servidor, String playerID, String idSessao, ObjectOutputStream out, ObjectInputStream in) {
        this.socket = socket;
        this.servidor = servidor;
        this.playerId = playerID;
        this.running = false;
        this.bnJogo = null;
        this.idSessao = idSessao;

        desconetado = false;
        tempoDesconetado = 0;
        System.out.println("teste");

        objectInputStream = in;
        objectOutputStream = out;


        System.out.println("A criar jogador PlayerID: " + playerID);
    }

    public void run(){
        running = true;

        while(running){
            running = readInput();
        }

        if (bnJogo != null) {
            bnJogo.removerJogador(this);
        }
    }

    public void setSocket(Socket socket, ObjectOutputStream out, ObjectInputStream in){
        this.socket = socket;
        tempoDesconetado = 0;
        try {

            objectInputStream.close();
            objectOutputStream.close();
            objectInputStream = in;
            objectOutputStream = out;

            this.writeInput(new Mensagem("Reconetado"));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public boolean isDesconetado() {
        return desconetado;
    }

    public void setDesconetado(Boolean connectado) {
        this.desconetado = connectado;
        if(desconetado) tempoDesconetado = System.currentTimeMillis();
    }

    public long getTempoDesconetado(){
        return tempoDesconetado;
    }

    public String getIdSessao(){
        return idSessao;
    }

    public boolean readInput(){
        try{
            Object input = objectInputStream.readObject();

            if(input instanceof EstadosMenu){
                return opcoesIniciais((EstadosMenu) input);
            }

            if(input instanceof Posicao){
                if(!bnJogo.condicaoesJogo()){
                    System.out.println("PlayerID: " + playerId + " Tiro indisponivel");
                    writeInput(new Mensagem("(Jogo não compre requesitos minimos)"));
                    writeInput(Validacao.INVALID_INPUT);
                    return true;
                }

                Posicao tiro = (Posicao) input;
                System.out.println("PlayerID: " + playerId +  " Tiro:(" + tiro.getX() + ", " + tiro.getY() + ")");
                if(bnJogo.turno(this)){
                    bnJogo.tiroTabuleiro(this, tiro);
                }else{
                    writeInput(new Mensagem("(Turno do oponente)"));
                    writeInput(Validacao.INVALID_INPUT);
                }

                return true;
            }

            if(input instanceof Tabuleiro){
                Tabuleiro tabuleiroBarcos = (Tabuleiro) input;
                bnJogo.addTabuleiro(this, tabuleiroBarcos);
                return true;
            }

        }catch(SocketException e){
            System.out.println("Ligação perdida com jogador PlayerID: " + playerId);
            running = false;
            return false;
        }catch (IOException e) {
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }


        writeInput(Validacao.ERROR);
        return true;
    }

    public void lerDadosCarregar(){
        try{
            Mensagem jogoID = (Mensagem) objectInputStream.readObject();
            boolean jogadorB = (Boolean) objectInputStream.readObject();

            BNJogo tempBNJogo = servidor.getBNJogo(jogoID.getMensagem() + "_carregado");

            if(tempBNJogo==null){
                System.out.println("1ND");
                BNJogo novoJogo = new BNJogo(this, servidor.criarJogoID());

                novoJogo.carregarJogo(jogoID.getMensagem(), jogadorB);
                servidor.addBNJogo(novoJogo);

                bnJogo = novoJogo;
                bnJogo.atulizarViews();
            }else{
                tempBNJogo.addJogador(this);

                bnJogo = tempBNJogo;
                bnJogo.atulizarViews();

                if(bnJogo.condicaoesJogo()){
                    bnJogo.jogoComecou(true);
                }
            }


        }catch(SocketException e){
            System.out.println("Ligação perdida com jogador PlayerID: " + playerId);
            running = false;
        }catch (IOException e) {
            e.printStackTrace();
            writeInput(Validacao.ERROR);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            writeInput(Validacao.ERROR);
        }



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
            setDesconetado(true);
            e.printStackTrace();
        }
    }

    private boolean opcoesIniciais(EstadosMenu estado){
        switch(estado){
            case NOVO_JOGO:
                BNJogo bnJogoDisponivel = servidor.getBNJogoDisponivel();

                if(bnJogoDisponivel == null){
                    criarNovoJogo();
                    //writeInput(Validacao.WAITING_INPUT);
                }else{
                    this.bnJogo = bnJogoDisponivel;
                    bnJogoDisponivel.addJogador(this);
                    //writeInput(Validacao.WAITING_INPUT);
                }

                break;
            case CARREGAR_JOGO:
                lerDadosCarregar();
                break;
            case GUARDAR:
                guardarJogo();
                break;
            case QUIT:
                return false;
        }

        return true;
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
