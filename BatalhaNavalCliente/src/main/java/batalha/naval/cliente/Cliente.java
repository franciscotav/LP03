package batalha.naval.cliente;

import library.payload.comunicacao.EstadosMenu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Cliente {
    private Socket socket;
    private ObjectOutputStream objectOuputStream;
    private ObjectInputStream objectInputStream;

    public Cliente(String host, int port){
        try{
            socket=new Socket(host,port);
            objectOuputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInput(EstadosMenu estadosMenu){
        try{
            objectOuputStream.writeObject(estadosMenu);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readInput(){
        try{
            return objectInputStream.readObject();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return null;
    }

    public void run(){
        while(true){

        }
    }

    public void find(){

    }
}
