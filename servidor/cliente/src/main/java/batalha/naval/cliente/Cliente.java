package batalha.naval.cliente;

import java.io.IOException;
import java.net.*;

public class Cliente {
    private Socket socket;

    public Cliente(String hostname, int port){
        try{
            socket=new Socket(hostname, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void find(){

    }
}
