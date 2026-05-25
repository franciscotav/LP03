package library.payload.comunicacao;

import java.io.Serializable;

public class Mensagem implements Serializable {
    private String mensagem;

    public Mensagem(String mensagem){
        this.mensagem=mensagem;
    }

    public String getMensagem(){
        return mensagem;
    }
}
