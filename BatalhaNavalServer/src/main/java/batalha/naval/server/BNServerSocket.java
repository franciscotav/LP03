package batalha.naval.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

public class BNServerSocket extends ServerSocket implements Runnable{
    private LinkedBlockingDeque<Socket> clients;

    public BNServerSocket(int porta) throws IOException{
        super(porta);
        clients = new LinkedBlockingDeque<>();
        System.out.println("Servidor: " + super.toString());
    }

    public void run(){
        listening();
    }

    private void listening(){
        while(true){
            try{
                Socket jogadorSocket = super.accept();
                clients.put(jogadorSocket);
                System.out.println("Nova Ligacao: " + jogadorSocket.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public LinkedBlockingDeque<Socket> clients(){
        return clients;
    }
}
