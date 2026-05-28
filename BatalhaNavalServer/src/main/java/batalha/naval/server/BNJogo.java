package batalha.naval.server;

import library.payload.comunicacao.EstadosJogo;
import library.payload.comunicacao.Mensagem;
import library.payload.tabuleiro.EstadosTabuleiro;
import library.payload.tabuleiro.Posicao;
import library.payload.tabuleiro.Tabuleiro;

import java.io.*;
import java.util.Random;

public class BNJogo {
    private String jogoID;

    private BNJogador jogadorA;
    private BNJogador jogadorB;

    private Tabuleiro jogadorATabuleiroBarcos;
    private Tabuleiro jogadorATabuleiroTiros;
    private Tabuleiro jogadorBTabuleiroBarcos;
    private Tabuleiro jogadorBTabuleiroTiros;

    private int tiros;

    private boolean started;
    private boolean terminou;

    private boolean jogadorAturno;
    private boolean jogadorBturno;

    public BNJogo(BNJogador bnJogador, String jogoID) {
        this.jogadorA = bnJogador;
        this.jogadorB = null;

        this.jogoID = jogoID;
        this.started = false;
        this.terminou = false;

        this.jogadorATabuleiroBarcos = null;
        this.jogadorATabuleiroTiros = new Tabuleiro();
        this.jogadorATabuleiroTiros.setTabuleiroTipoTiro(true);
        this.jogadorBTabuleiroBarcos = null;
        this.jogadorBTabuleiroTiros = new Tabuleiro();
        this.jogadorBTabuleiroTiros.setTabuleiroTipoTiro(true);

        this.tiros = 3;

        this.jogadorAturno = false;
        this.jogadorBturno = false;

        aleatorioJogador();

        System.out.println("A criar jogo JogoID: " + jogoID);
    }

    public synchronized boolean esperaJogador() {
        if (jogadorA == null || jogadorB == null)
            return true;

        return false;
    }

    public void jogoComecou(boolean bool){
        started = bool;
    }

    public synchronized void addJogador(BNJogador bnJogador) {
        boolean addJogador = false;

        if (jogadorA == null) {
            jogadorA = bnJogador;
            addJogador = true;
        }

        if (jogadorB == null && addJogador == false) {
            jogadorB = bnJogador;
            addJogador = true;
        }

        if (jogadorB != null && jogadorA != null) {
            jogadorB.writeInput(EstadosJogo.OPONENTE_CONECTION);
            jogadorA.writeInput(EstadosJogo.OPONENTE_CONECTION);
        }

        System.out.println("GameID: " + jogoID + " Addicionar PlayerID: " + bnJogador.getPlayerID());

    }

    public String getJogoId() {
        return jogoID;
    }

    public void guardarJogo() {
        String path = "saves\\" + jogoID;

        Dados dados = new Dados(jogadorATabuleiroBarcos, jogadorATabuleiroTiros, jogadorBTabuleiroBarcos, jogadorBTabuleiroTiros, jogadorAturno, jogadorBturno, tiros);
        Dados.write(path,dados);

        if (jogadorA != null){
            jogadorA.writeInput(new Mensagem("Jogo guardado:"));
            jogadorA.writeInput(new Mensagem("JogoID: " + jogoID + " (Jogador A)"));
        }

        if (jogadorB != null){
            jogadorB.writeInput(new Mensagem("Jogo guardado:"));
            jogadorB.writeInput(new Mensagem("JogoID: " + jogoID + " (Jogador B)"));
        }

    }

    public void carregarJogo(String jogoID, boolean ecolherJogadorB) {
        if (ecolherJogadorB) {
            jogadorB = jogadorA;
            jogadorA = null;
        }

        String path = "saves\\" + jogoID;

        Dados dados = Dados.read(path);
        if(dados == null)
            return;

        this.jogadorATabuleiroBarcos = dados.getJogadorATabuleiroBarcos();
        this.jogadorATabuleiroTiros = dados.getJogadorATabuleiroTiros();
        this.jogadorBTabuleiroBarcos = dados.getJogadorBTabuleiroBarcos();
        this.jogadorBTabuleiroTiros = dados.getJogadorBTabuleiroTiros();
        this.jogadorAturno = dados.getJogadorAturno();
        this.jogadorBturno = dados.getJogadorBturno();
        this.tiros = dados.getTiros();

        this.jogoID = jogoID + "_carregado";


        if (jogadorA != null)
            jogadorA.writeInput(new Mensagem("Jogo carregado!"));
        if (jogadorB != null)
            jogadorB.writeInput(new Mensagem("Jogo carregado!"));
    }

    public void atulizarViews() {
        if (jogadorA != null) {
            jogadorA.writeInput(jogadorATabuleiroBarcos);
            jogadorA.writeInput(jogadorATabuleiroTiros);
        }

        if (jogadorB != null) {
            jogadorB.writeInput(jogadorBTabuleiroBarcos);
            jogadorB.writeInput(jogadorBTabuleiroTiros);
        }

    }

    public synchronized void addTabuleiro(BNJogador bnJogador, Tabuleiro tabuleiroBarcos) {
        if (bnJogador == jogadorA) {
            jogadorATabuleiroBarcos = tabuleiroBarcos;
            // jogadorATabuleiroBarcos.imprime();
        }

        if (bnJogador == jogadorB) {
            jogadorBTabuleiroBarcos = tabuleiroBarcos;
            // jogadorBTabuleiroBarcos.imprime();
        }

        if (jogadorATabuleiroBarcos != null &&
                jogadorBTabuleiroBarcos != null) {

            started = true;
            jogadorA.writeInput(new Mensagem("Comecou a jogo!"));
            jogadorB.writeInput(new Mensagem("Comecou a jogo!"));
            if (turno(jogadorA)) {
                jogadorA.writeInput(new Mensagem("Teu turno!"));
                jogadorB.writeInput(new Mensagem("Turno do adversario!"));
            } else {
                jogadorB.writeInput(new Mensagem("Teu turno!"));
                jogadorA.writeInput(new Mensagem("Turno do adversario!"));
            }
        }

    }

    public boolean turno(BNJogador bnJogador) {
        if (jogadorAturno) {
            if (jogadorA == bnJogador)
                return true;
        } else {
            if (jogadorB == bnJogador)
                return true;
        }

        return false;
    }

    public synchronized void tiroTabuleiro(BNJogador bnJogador, Posicao tiro) {
        if (!started || terminou) {
            bnJogador.writeInput(new Mensagem("Jogo não está a decorrer"));
            return;
        }

        if (!turno(bnJogador)) {
            bnJogador.writeInput(new Mensagem("Não é o teu turno"));
            return;
        }

        if (jogadorA == bnJogador) {
            // verificar que já não tinhamos mandado um tiro lá
            if (jogadorATabuleiroTiros.verificarTiro(tiro)) {
                EstadosTabuleiro estadoTabuleiro = jogadorBTabuleiroBarcos.getEstadosTabuleiro(tiro.getX(),
                        tiro.getY());

                // se acertar num barco
                if (estadoTabuleiro != EstadosTabuleiro.MAR) {
                    jogadorATabuleiroTiros.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ACERTOU);
                    jogadorBTabuleiroBarcos.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.DANO);

                    jogadorA.writeInput(jogadorATabuleiroTiros);
                    // jogadorATabuleiroTiros.imprime();
                    jogadorB.writeInput(jogadorBTabuleiroBarcos);
                    bnJogador.writeInput(new Mensagem("Acertou!"));
                    jogadorB.writeInput(new Mensagem("Barco atingido!"));
                    if (!jogadorBTabuleiroBarcos.barcoExiste(estadoTabuleiro)) {
                        bnJogador.writeInput(new Mensagem(estadoTabuleiro.name() + " destruido!"));
                        jogadorB.writeInput(new Mensagem(estadoTabuleiro.name() + " afundou!"));
                    }
                    if (jogadorBTabuleiroBarcos.semBarcos()) {
                        bnJogador.writeInput(new Mensagem("Vitoria!"));
                        jogadorB.writeInput(new Mensagem("Derrota!"));
                        terminou = true;
                        return;
                    }
                    alternarTurno();
                    return;
                } else {// se errar
                    jogadorATabuleiroTiros.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ERROU);
                    jogadorBTabuleiroBarcos.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ERROU);
                    jogadorA.writeInput(jogadorATabuleiroTiros);
                    jogadorB.writeInput(jogadorBTabuleiroBarcos);
                    bnJogador.writeInput(new Mensagem("Tiro no Mar!"));
                    jogadorB.writeInput(new Mensagem("Adversario atirou no mar!"));
                    alternarTurno();
                    return;
                }
            }

            bnJogador.writeInput(new Mensagem("Já foi selecionada!"));
            return;
        }

        // igual mas para o B
        if (jogadorB == bnJogador) {
            // verificar que já não tinhamos mandado um tiro lá
            if (jogadorBTabuleiroTiros.verificarTiro(tiro)) {
                EstadosTabuleiro estadoTabuleiro = jogadorATabuleiroBarcos.getEstadosTabuleiro(tiro.getX(),
                        tiro.getY());

                // verificar o que está lá
                if (estadoTabuleiro != EstadosTabuleiro.MAR) {
                    jogadorBTabuleiroTiros.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ACERTOU);
                    jogadorATabuleiroBarcos.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.DANO);
                    jogadorB.writeInput(jogadorBTabuleiroTiros);
                    jogadorA.writeInput(jogadorATabuleiroBarcos);

                    bnJogador.writeInput(new Mensagem("Acertou!"));
                    jogadorA.writeInput(new Mensagem("Barco atingido!"));

                    if (!jogadorATabuleiroBarcos.barcoExiste(estadoTabuleiro)) {
                        bnJogador.writeInput(new Mensagem(estadoTabuleiro.name() + " destruido!"));
                        jogadorA.writeInput(new Mensagem(estadoTabuleiro.name() + " afundou!"));
                    }
                    if (jogadorATabuleiroBarcos.semBarcos()) {
                        bnJogador.writeInput(new Mensagem("Vitoria!"));
                        jogadorA.writeInput(new Mensagem("Derrota!"));
                        terminou = true;
                        return;
                    }
                    alternarTurno();
                    return;
                } else {
                    jogadorBTabuleiroTiros.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ERROU);
                    jogadorATabuleiroBarcos.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ERROU);
                    jogadorB.writeInput(jogadorBTabuleiroTiros);
                    jogadorA.writeInput(jogadorATabuleiroBarcos);
                    bnJogador.writeInput(new Mensagem("Tiro no Mar!"));
                    jogadorA.writeInput(new Mensagem("Adversario atirou no mar!"));
                    alternarTurno();
                    return;
                }
            }

            bnJogador.writeInput(new Mensagem("Já foi selecionada!"));
            return;
        }

        bnJogador.writeInput(new Mensagem("Tiro inválido!"));
    }

    private void aleatorioJogador() {
        Random random = new Random();
        if (random.nextBoolean()) {
            jogadorAturno = true;
            jogadorBturno = false;

        } else {
            jogadorAturno = false;
            jogadorBturno = true;
        }
    }

    private synchronized void alternarTurno() {
        // se não houver tiros troca, se houver tira 1
        if (tiros == 1) {
            jogadorAturno = (!jogadorAturno);
            jogadorBturno = (!jogadorBturno);
            tiros = 3;
        } else {
            tiros--;
        }

        if (jogadorAturno) {
            jogadorA.writeInput(new Mensagem("Teu turno!"));
            jogadorA.writeInput(new Mensagem("Numero de tiros: " + tiros));
            jogadorB.writeInput(new Mensagem("Turno do adversario!"));
        } else {
            jogadorB.writeInput(new Mensagem("Teu turno!"));
            jogadorB.writeInput(new Mensagem("Numero de tiros: " + tiros));
            jogadorA.writeInput(new Mensagem("Turno do adversario!"));
        }

    }

    public boolean condicaoesJogo() {
        if (jogadorA == null || jogadorB == null)
            return false;

        if (jogadorATabuleiroBarcos == null || jogadorBTabuleiroBarcos == null)
            return false;

        return true;
    }

    public synchronized void vitoriaPorDesistencia(BNJogador desistente) {
        if (terminou) return;

        BNJogador vencedor = getOponente(desistente);
        if (vencedor != null) {
            vencedor.writeInput(new Mensagem("Vitoria por desistência do oponente!"));
            desistente.writeInput(new Mensagem("Derrota por tempo limite excedido!"));
        }
        terminou = true;
        started = false;
    }

    public BNJogador getOponente(BNJogador jogador) {
        if (jogador == jogadorA) return jogadorB;
        if (jogador == jogadorB) return jogadorA;
        return null;
    }

    public boolean isTerminou() {
        return terminou;
    }

    public synchronized void removerJogador(BNJogador bnJogador) {
        if (jogadorA == bnJogador) {
            jogadorA = null;
            System.out.println("GameID: " + jogoID + " Remover PlayerID: " + bnJogador.getPlayerID());
        }

        if (jogadorB == bnJogador) {
            jogadorB = null;
            System.out.println("GameID: " + jogoID + " Remover PlayerID: " + bnJogador.getPlayerID());
        }

        if (jogadorA == null && jogadorB == null) {
            started = false;
            System.out.println("GameID: " + jogoID + " Sem Jogadores");
        }
    }

}

class Dados implements Serializable{
    private Tabuleiro jogadorATabuleiroBarcos;
    private Tabuleiro jogadorATabuleiroTiros;
    private Tabuleiro jogadorBTabuleiroBarcos;
    private Tabuleiro jogadorBTabuleiroTiros;
    private Boolean jogadorAturno;
    private Boolean jogadorBturno;
    private Integer tiros;

    public Dados(Tabuleiro jogadorATabuleiroBarcos, Tabuleiro jogadorATabuleiroTiros, Tabuleiro jogadorBTabuleiroBarcos, Tabuleiro jogadorBTabuleiroTiros, Boolean jogadorAturno, Boolean jogadorBturno, Integer tiros) {
        this.jogadorATabuleiroBarcos = jogadorATabuleiroBarcos;
        this.jogadorATabuleiroTiros = jogadorATabuleiroTiros;
        this.jogadorBTabuleiroBarcos = jogadorBTabuleiroBarcos;
        this.jogadorBTabuleiroTiros = jogadorBTabuleiroTiros;
        this.jogadorAturno = jogadorAturno;
        this.jogadorBturno = jogadorBturno;
        this.tiros = tiros;
    }

    public Integer getTiros() {
        return tiros;
    }

    public Boolean getJogadorBturno() {
        return jogadorBturno;
    }

    public Boolean getJogadorAturno() {
        return jogadorAturno;
    }

    public Tabuleiro getJogadorBTabuleiroTiros() {
        return jogadorBTabuleiroTiros;
    }

    public Tabuleiro getJogadorBTabuleiroBarcos() {
        return jogadorBTabuleiroBarcos;
    }

    public Tabuleiro getJogadorATabuleiroTiros() {
        return jogadorATabuleiroTiros;
    }

    public Tabuleiro getJogadorATabuleiroBarcos() {
        return jogadorATabuleiroBarcos;
    }

    public static void write(String path, Dados dados){
        try{
            File file = new File(path);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream outObj = new ObjectOutputStream(out);
            outObj.writeObject(dados);

            outObj.close();

            System.out.println("Gravou Jogo: " + path);
        }catch (FileNotFoundException e){
            System.out.println("Não conseguiu criar ficherio para escrever: " + path);
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static Dados read(String path){
        try{
            File file = new File(path);

            FileInputStream in = new FileInputStream(file);
            ObjectInputStream inObj = new ObjectInputStream(in);
            Dados dados = (Dados) inObj.readObject();

            inObj.close();

            System.out.println("Carregou Jogo: " + path);
            return dados;

        }catch (FileNotFoundException e){
            System.out.println("Ficheiro não encontrado para ler: " + path);
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

}
