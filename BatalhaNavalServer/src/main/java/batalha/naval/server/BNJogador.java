package batalha.naval.server;

import java.net.Socket;

public class BNJogador implements Runnable {
    private Socket socket;

    public BNJogador(Socket socket){
        this.socket = socket;
    }

    public void run(){
        while(true){
            readInput();
        }
    }

    private void readInput(){

    }
}
