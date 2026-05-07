package batalha.naval.cliente.tabuleiro;

public class Tabuleiro {
    private EstadosTabuleiro[][] ceculas;

    public Tabuleiro(){
        ceculas = new EstadosTabuleiro[10][10];

        //popular tabuleiro com MAR
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                ceculas[i][j]=EstadosTabuleiro.MAR;
            }
        }
    }

    public void adicionarBarco(){

    }
}
