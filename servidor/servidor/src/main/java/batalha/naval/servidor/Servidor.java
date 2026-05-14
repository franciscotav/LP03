package batalha.naval.servidor;

import java.io.IOException;
import java.net.*;


public class Servidor {
    private ServerSocket server;

    public Servidor(int port){
        try{server= new ServerSocket(port);} catch(IOException e){}
    }

    public void listening(){
        Socket clienteA;
        Socket clienteB;

        try{
            clienteA = server.accept();
            clienteB = server.accept();

            Thread jogo = new Thread(new BatalhaNaval(clienteA, clienteB));
            jogo.start();

            clienteA.close();
            clienteB.close();


        } catch(IOException e){}


    }


}
