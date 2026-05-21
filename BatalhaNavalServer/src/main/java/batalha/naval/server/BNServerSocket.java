package batalha.naval.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class BNServerSocket extends ServerSocket implements Runnable{
    private LinkedList<Socket> clients;

    public BNServerSocket(int porta) throws IOException{
        super(porta);
        clients = new LinkedList<>();

    }

    public void run(){
        listening();
    }

    private void listening(){
        while(true){
            try{
                Socket cliente = super.accept();
                clients.add(cliente);
                System.out.println("Cliente: " + cliente.getLocalAddress() + ":" + cliente.getLocalPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public LinkedList<Socket> clients(){
        return clients;
    }
}
