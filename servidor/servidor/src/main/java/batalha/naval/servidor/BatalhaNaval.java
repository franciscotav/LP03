package batalha.naval.servidor;

import java.net.*;

public class BatalhaNaval implements Runnable{
    private Socket clienteA;
    private Socket clienteB;

    public BatalhaNaval(Socket clienteA, Socket clienteB){
        this.clienteA = clienteA;
        this.clienteB = clienteB;
    }

    @Override
    public void run(){

    }
}
