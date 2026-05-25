package batalha.naval.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

public class BNServerSocket extends ServerSocket implements Runnable{
    private LinkedBlockingDeque<Socket> clients;

    public BNServerSocket(int porta) throws IOException{
        super(porta);
        clients = new LinkedBlockingDeque<>();

        InetAddress remoteAddress = super.getInetAddress();
        String remoteIp = remoteAddress.getHostAddress();
        System.out.println("Servidor: " + remoteIp + ":" + porta);
    }

    public void run(){
        listening();
    }

    private void listening(){
        while(true){
            try{
                Socket jogadorSocket = super.accept();
                clients.put(jogadorSocket);

                InetAddress localAddress = jogadorSocket.getLocalAddress();
                String localIp = localAddress.getHostAddress();
                int localPort = jogadorSocket.getLocalPort();

                System.out.println("Nova Ligacao: " + localIp + ":" + localPort);
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
