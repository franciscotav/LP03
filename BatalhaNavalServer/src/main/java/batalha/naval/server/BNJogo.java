package batalha.naval.server;

public class BNJogo implements Runnable{
    private BNJogador jogadorA;
    private BNJogador jogadorB;

    public BNJogo(BNJogador bnJogador){
        this.jogadorA = bnJogador;
        this.jogadorB = null;
    }

    public boolean esperaJogador(){
        if(jogadorB == null)
            return true;

        return false;
    }

    public void addJogador(BNJogador bnJogador){
        jogadorB = bnJogador;
    }

    public void run() {
        while(true){
            if(jogadorB != null)
        }
    }

    public void removerJogador(){
    }
}
