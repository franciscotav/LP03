package batalha.naval.cliente.barco;

public abstract class Barco {
    private String nome;
    private int comprimento;

    public Barco(String nome, int comprimento){
        this.nome=nome;
        this.comprimento=comprimento;
    }
}
