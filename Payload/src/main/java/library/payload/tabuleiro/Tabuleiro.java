package library.payload.tabuleiro;

import library.payload.barco.*;

import java.io.Serializable;

public class Tabuleiro implements Serializable {
    private EstadosTabuleiro[][] celulas;
    private boolean tabuleiroTipoTiro;

    public Tabuleiro(){
        celulas = new EstadosTabuleiro[10][10];
        tabuleiroTipoTiro = false;

        //popular tabuleiro com MAR
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                celulas[i][j]=EstadosTabuleiro.MAR;
            }
        }
    }

    public boolean isTabuleiroTipoTiro(){
        return tabuleiroTipoTiro;
    }

    public void setTabuleiroTipoTiro(Boolean bool){
        tabuleiroTipoTiro = bool;
    }


    public void setEstadosTabuleiro(int i, int j, EstadosTabuleiro estadosTabuleiro){
        celulas[i][j] = estadosTabuleiro;
    }

    public EstadosTabuleiro getEstadoTabuleiro(int i, int j){
        return celulas[i][j];
    }

    public EstadosTabuleiro getEstadosTabuleiro(int i, int j){
        return celulas[i][j];
    }

    public boolean adicionarBarco(Barco barco, Posicao posicao, Orientacao orientacao){
        //se todos os requisitos para colocar o barco forem válidos!
        if(!(this.verificaLimites(barco, posicao, orientacao) && this.verificaProximidade(barco, posicao, orientacao))){
            return false;
        }
        //caso seja true
        //colocar peças no tabuleiro
        int comprimento = barco.getComprimento();
        Posicao posicaoAtual;

        int x = posicao.getX();
        int y = posicao.getY();

        for (int i = 0; i < comprimento; i++) {
            //faz as contas consuante a orientação
            switch(orientacao){
                case NORTE:
                    posicaoAtual= new Posicao(x, y-i);
                    break;
                case ESTE:
                    posicaoAtual= new Posicao(x+i, y);
                    break;
                case SUL:
                    posicaoAtual= new Posicao(x, y+i);
                    break;
                case OESTE:
                    posicaoAtual= new Posicao(x-i, y);
                    break;
                //para o compilador não chatear!
                default:
                posicaoAtual = new Posicao(0, 0);
            }

            celulas[posicaoAtual.getX()][posicaoAtual.getY()] = barco.getTipo();
        }

        return true;
    }

    //verifica se está dentro dos limites do tabuleiro
    public boolean verificaLimites(Barco barco, Posicao posicao, Orientacao orientacao){
        if (posicao.getX() < 0 || posicao.getX() >= 10 || posicao.getY() < 0 || posicao.getY() >= 10) {
            return false;
        }

        int posicaoFinal;
        switch(orientacao){
            case NORTE:
                posicaoFinal = posicao.getY() - (barco.getComprimento() - 1);
                return posicaoFinal >= 0;
            case ESTE:
                posicaoFinal = posicao.getX() + (barco.getComprimento() - 1);
                return posicaoFinal < 10;
            case SUL:
                posicaoFinal = posicao.getY() + (barco.getComprimento() - 1);
                return posicaoFinal < 10;
            case OESTE:
                posicaoFinal = posicao.getX() - (barco.getComprimento() - 1);
                return posicaoFinal >= 0;
            default:
                return false;
        }
    }

    //verifica se tem algum barco perto á volta
    private boolean verificaProximidade(Barco barco, Posicao posicao, Orientacao orientacao){
        int x = posicao.getX();
        int y = posicao.getY();

        int dim = barco.getComprimento();

        Posicao posicaoAtual;

        for (int i = 0; i < dim; i++) {
            switch(orientacao){
                case NORTE:
                    posicaoAtual= new Posicao(x, y-i);
                    break;
                case ESTE:
                    posicaoAtual= new Posicao(x+i, y);
                    break;
                case SUL:
                    posicaoAtual= new Posicao(x, y+i);
                    break;
                case OESTE:
                    posicaoAtual= new Posicao(x-i, y);
                    break;
                //para o compilador não chatear
                default:
                    posicaoAtual = new Posicao(0, 0);
            }

            boolean validacao = verificaVizinho(posicaoAtual);

            if(!validacao){
                return false;
            }
        }

        return true;
    }

    //verifica se as posições adejacentes estão vazias, de um quadrado singular
    private boolean verificaVizinho(Posicao posicao){
        boolean valido = true;
        int x = posicao.getX()-1;
        int y = posicao.getY()-1;
        
        //atribuir o valor do x-1 ao i e partir daí, rodar as proximas 3 casas para a direita incluindo a casa atual
        for (int i = x; i < x+3; i++) {
            //se o "i" sair do tabuleiro, continua para a proxima casa aválida
            if(i<0 || i>=10) continue;
            for (int j = y; j < y+3; j++) {
                if(j<0 || j>=10) continue;
                if(!celulas[i][j].equals(EstadosTabuleiro.MAR)){
                    return false;
                }
            }
        }

        return valido;
    }

    //para testar
    public void imprime(){
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                System.out.print(celulas[i][j].ordinal() + " ");
            }
            System.out.println();
        }
    }

    public boolean barcoExiste(EstadosTabuleiro barco) {
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (celulas[i][j] == barco)
                    return true;
            }
        }

        return false;
    }

    public boolean semBarcos(){
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                EstadosTabuleiro estado = celulas[i][j];
                if(estado != EstadosTabuleiro.MAR &&
                        estado != EstadosTabuleiro.DANO &&
                        estado != EstadosTabuleiro.ERROU
                ){
                    return false;
                }

            }
        }

        return true;
    }

    public boolean verificarTiro(Posicao posicao){
        if(celulas[posicao.getX()][posicao.getY()] == EstadosTabuleiro.MAR){
            return true;
        }

        return false;
    }
}
