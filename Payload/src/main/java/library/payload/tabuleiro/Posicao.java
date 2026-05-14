package library.payload.tabuleiro;

public class Posicao {
    private int x;
    private int y;

    public Posicao(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }
}
