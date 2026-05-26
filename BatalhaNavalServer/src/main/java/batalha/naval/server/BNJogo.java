package batalha.naval.server;

import library.payload.comunicacao.EstadosJogo;
import library.payload.comunicacao.Mensagem;
import library.payload.tabuleiro.EstadosTabuleiro;
import library.payload.tabuleiro.Posicao;
import library.payload.tabuleiro.Tabuleiro;

import java.util.Random;

public class BNJogo{
    private String jogoID;

    private BNJogador jogadorA;
    private BNJogador jogadorB;

    private Tabuleiro jogadorATabuleiroBarcos;
    private Tabuleiro jogadorATabuleiroTiros;
    private Tabuleiro jogadorBTabuleiroBarcos;
    private Tabuleiro jogadorBTabuleiroTiros;

    private int tiros;

    private boolean running;

    private boolean jogadorAturno;
    private boolean jogadorBturno;

    public BNJogo(BNJogador bnJogador, String jogoID){
        this.jogadorA = bnJogador;
        this.jogadorB = null;

        this.jogoID = jogoID;
        this.running = false;

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

    public synchronized boolean esperaJogador(){
        if(jogadorA == null || jogadorB == null)
            return true;

        return false;
    }

    public synchronized void addJogador(BNJogador bnJogador){
        boolean addJogador = false;

        if(jogadorA == null){
            jogadorA = bnJogador;
            addJogador = true;
        }

        if(jogadorB == null && addJogador == false){
            jogadorB = bnJogador;
            addJogador = true;
        }

        if(jogadorB != null && jogadorA != null){

            jogadorB.writeInput(EstadosJogo.OPONENTE_CONECTION);
            jogadorA.writeInput(EstadosJogo.OPONENTE_CONECTION);
        }


        System.out.println("GameID: " + jogoID + " Addicionar PlayerID: " + bnJogador.getPlayerID());

    }

    public synchronized void addTabuleiro(BNJogador bnJogador, Tabuleiro tabuleiroBarcos){
        if(bnJogador == jogadorA){
            jogadorATabuleiroBarcos = tabuleiroBarcos;
            jogadorATabuleiroBarcos.imprime();
        }

        if(bnJogador == jogadorB){
            jogadorBTabuleiroBarcos = tabuleiroBarcos;
            jogadorBTabuleiroBarcos.imprime();
        }
    }

    public boolean turno(BNJogador bnJogador){
        if(jogadorAturno){
            if(jogadorA == bnJogador)
                return true;
        }else{
            if(jogadorB == bnJogador)
                return true;
        }

        return false;
    }

    public synchronized void tiroTabuleiro(BNJogador bnJogador, Posicao tiro){
        if (!turno(bnJogador)) {
            bnJogador.writeInput(new Mensagem("Não é o teu turno"));
        }

        if(jogadorA == bnJogador){
                //verificar que já não tinhamos mandado um tiro lá
                if(jogadorATabuleiroTiros.verificarTiro(tiro)){
                    EstadosTabuleiro estadoTabuleiro = jogadorBTabuleiroBarcos.getEstadosTabuleiro(tiro.getX(), tiro.getY());

                    //se acertar num barco
                    if(estadoTabuleiro!=EstadosTabuleiro.MAR){
                        jogadorATabuleiroTiros.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ACERTOU);
                        jogadorBTabuleiroBarcos.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.DANO);

                        jogadorA.writeInput(jogadorATabuleiroTiros);
                        //jogadorATabuleiroTiros.imprime();
                        jogadorB.writeInput(jogadorBTabuleiroBarcos);
                        alternarTurno();
                        bnJogador.writeInput(new Mensagem("Acertou"));
                        return;
                    }else{//se errar
                        jogadorATabuleiroTiros.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ERROU);
                        jogadorBTabuleiroBarcos.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ERROU);
                        jogadorA.writeInput(jogadorATabuleiroTiros);
                        jogadorB.writeInput(jogadorBTabuleiroBarcos);
                        alternarTurno();
                        bnJogador.writeInput(new Mensagem("Errou"));
                        return;
                    }
                }

                bnJogador.writeInput(new Mensagem("Já foi escolhido"));
                return;
            }

            //igual mas para o B
        if(jogadorB == bnJogador){
            //verificar que já não tinhamos mandado um tiro lá
            if(jogadorBTabuleiroTiros.verificarTiro(tiro)){
                EstadosTabuleiro estadoTabuleiro = jogadorATabuleiroBarcos.getEstadosTabuleiro(tiro.getX(), tiro.getY());

                //verificar o que está lá
                if(estadoTabuleiro!=EstadosTabuleiro.MAR){
                    jogadorBTabuleiroTiros.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ACERTOU);
                    jogadorATabuleiroBarcos.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.DANO);
                    jogadorB.writeInput(jogadorBTabuleiroTiros);
                    jogadorA.writeInput(jogadorATabuleiroBarcos);
                    alternarTurno();
                    bnJogador.writeInput(new Mensagem("Acertou"));
                    return;
                }else{
                    jogadorBTabuleiroTiros.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ERROU);
                    jogadorATabuleiroBarcos.setEstadosTabuleiro(tiro.getX(), tiro.getY(), EstadosTabuleiro.ERROU);
                    jogadorB.writeInput(jogadorBTabuleiroTiros);
                    jogadorA.writeInput(jogadorATabuleiroBarcos);
                    alternarTurno();
                    bnJogador.writeInput(new Mensagem("Errou"));
                    return;
                }
            }

            bnJogador.writeInput(new Mensagem("Já foi escolhido"));
            return;
        }

        bnJogador.writeInput(new Mensagem("Tiro inválido"));
    }

    private void aleatorioJogador(){
        Random random = new Random();
        if(random.nextBoolean()){
            jogadorAturno = true;
            jogadorBturno = false;

        }else{
            jogadorAturno = false;
            jogadorBturno = true;
        }
    }

    private synchronized void alternarTurno(){
        //se não houver tiros troca, se houver tira 1
        if(tiros==1){
            jogadorAturno = ( !jogadorAturno );
            jogadorBturno = ( !jogadorBturno );
            this.tiros = 3;
        }else{
            tiros--;
        }

    }

    public boolean condicaoesJogo(){
        if(jogadorA == null || jogadorB == null)
            return false;

        if(jogadorATabuleiroBarcos == null || jogadorBTabuleiroBarcos == null)
            return false;

        return true;
    }

    public synchronized void removerJogador(BNJogador bnJogador){
        if(jogadorA == bnJogador){
            jogadorA = null;
            System.out.println("GameID: " + jogoID + " Remover PlayerID: " + bnJogador.getPlayerID());
        }

        if(jogadorB == bnJogador){
            jogadorB = null;
            System.out.println("GameID: " + jogoID + " Remover PlayerID: " + bnJogador.getPlayerID());
        }

        if(jogadorA == null && jogadorB == null){
            running = false;
            System.out.println("GameID: " + jogoID + " Sem Jogadores");
        }
    }

}
