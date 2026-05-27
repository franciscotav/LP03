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
        try {
            String path = "saves\\" + jogoID + "\\";

            String[] ficheiros = {"jogadorATabuleiroBarcos",
                    "jogadorATabuleiroTiros",
                    "jogadorAturno",
                    "jogadorBTabuleiroBarcos",
                    "jogadorBTabuleiroTiros",
                    "jogadorBturno",
                    "tiros",
                    "started",
                    "terminou"};

            String finalPath = path + ficheiros[0];
            FileOutputStream out = new FileOutputStream(finalPath);
            ObjectOutputStream outObj = new ObjectOutputStream(out);
            outObj.writeObject(jogadorATabuleiroBarcos);
            out.close();
            outObj.close();

            finalPath = path + ficheiros[1];
            out = new FileOutputStream(finalPath);
            outObj = new ObjectOutputStream(out);
            outObj.writeObject(jogadorATabuleiroTiros);
            out.close();
            outObj.close();

            finalPath = path + ficheiros[2];
            out = new FileOutputStream(finalPath);
            outObj = new ObjectOutputStream(out);
            outObj.writeObject((Boolean) jogadorAturno);
            out.close();
            outObj.close();

            finalPath = path + ficheiros[3];
            out = new FileOutputStream(finalPath);
            outObj = new ObjectOutputStream(out);
            outObj.writeObject(jogadorBTabuleiroBarcos);
            out.close();
            outObj.close();

            finalPath = path + ficheiros[4];
            out = new FileOutputStream(finalPath);
            outObj = new ObjectOutputStream(out);
            outObj.writeObject(jogadorBTabuleiroTiros);
            out.close();
            outObj.close();

            finalPath = path + ficheiros[5];
            out = new FileOutputStream(finalPath);
            outObj = new ObjectOutputStream(out);
            outObj.writeObject((Boolean) jogadorBturno);
            out.close();
            outObj.close();

            finalPath = path + ficheiros[6];
            out = new FileOutputStream(finalPath);
            outObj = new ObjectOutputStream(out);
            outObj.writeObject((Integer) tiros);
            out.close();
            outObj.close();

            finalPath = path + ficheiros[7];
            out = new FileOutputStream(finalPath);
            outObj = new ObjectOutputStream(out);
            outObj.writeObject((Boolean) started);
            out.close();
            outObj.close();

            finalPath = path + ficheiros[8];
            out = new FileOutputStream(finalPath);
            outObj = new ObjectOutputStream(out);
            outObj.writeObject((Boolean) terminou);
            out.close();
            outObj.close();


            if (jogadorA != null)
                jogadorA.writeInput(new Mensagem("Jogo guardado!"));
            if (jogadorB != null)
                jogadorB.writeInput(new Mensagem("Jogo guardado!"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void carregarJogo(String jogoID, boolean ecolherJogadorB) {
        try {
            if (ecolherJogadorB) {
                jogadorB = jogadorA;
                jogadorA = null;
            }

            this.jogoID = jogoID + "_carregado";

            String path = "saves\\" + jogoID + "\\";

            String[] ficheiros = {"jogadorATabuleiroBarcos",
                    "jogadorATabuleiroTiros",
                    "jogadorAturno",
                    "jogadorBTabuleiroBarcos",
                    "jogadorBTabuleiroTiros",
                    "jogadorBturno",
                    "tiros",
                    "started",
                    "terminou"};

            String finalPath = path + ficheiros[0];
            FileInputStream in = new FileInputStream(finalPath);
            ObjectInputStream inObj = new ObjectInputStream(in);
            jogadorATabuleiroBarcos = (Tabuleiro) inObj.readObject();
            in.close();
            inObj.close();

            finalPath = path + ficheiros[1];
            in = new FileInputStream(finalPath);
            inObj = new ObjectInputStream(in);
            jogadorATabuleiroTiros = (Tabuleiro) inObj.readObject();
            in.close();
            inObj.close();

            finalPath = path + ficheiros[2];
            in = new FileInputStream(finalPath);
            inObj = new ObjectInputStream(in);
            jogadorAturno = (Boolean) inObj.readObject();
            in.close();
            inObj.close();

            finalPath = path + ficheiros[3];
            in = new FileInputStream(finalPath);
            inObj = new ObjectInputStream(in);
            jogadorBTabuleiroBarcos = (Tabuleiro) inObj.readObject();
            in.close();
            inObj.close();

            finalPath = path + ficheiros[4];
            in = new FileInputStream(finalPath);
            inObj = new ObjectInputStream(in);
            jogadorBTabuleiroTiros = (Tabuleiro) inObj.readObject();
            in.close();
            inObj.close();

            finalPath = path + ficheiros[5];
            in = new FileInputStream(finalPath);
            inObj = new ObjectInputStream(in);
            jogadorBturno = (Boolean) inObj.readObject();
            in.close();
            inObj.close();

            finalPath = path + ficheiros[6];
            in = new FileInputStream(finalPath);
            inObj = new ObjectInputStream(in);
            tiros = (Integer) inObj.readObject();
            in.close();
            inObj.close();

            finalPath = path + ficheiros[7];
            in = new FileInputStream(finalPath);
            inObj = new ObjectInputStream(in);
            started = (Boolean) inObj.readObject();
            in.close();
            inObj.close();

            finalPath = path + ficheiros[8];
            in = new FileInputStream(finalPath);
            inObj = new ObjectInputStream(in);
            terminou = (Boolean) inObj.readObject();
            in.close();
            inObj.close();

            if (jogadorA != null)
                jogadorA.writeInput(new Mensagem("Jogo carregado!"));
            if (jogadorB != null)
                jogadorB.writeInput(new Mensagem("Jogo carregado!"));

            inObj.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void atulizarView() {
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
