package batalha.naval.server;

public class BNJogo implements Runnable{
    private String jogoID;
    private BNJogador jogadorA;
    private BNJogador jogadorB;

    public BNJogo(BNJogador bnJogador, String jogoID){
        this.jogadorA = bnJogador;
        this.jogadorB = null;

        this.jogoID = jogoID;
    }

    public synchronized boolean esperaJogador(){
        if(jogadorA == null || jogadorB == null)
            return true;

        return false;
    }

    public synchronized void addJogador(BNJogador bnJogador){
        if(jogadorA == null)
            jogadorA = bnJogador;
        else
            jogadorB = bnJogador;
    }

    public void run() {
        while(true){
            //if(jogadorB != null)
        }
    }

    public synchronized void removerJogador(){
    }
}
