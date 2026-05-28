package batalha.naval.cliente;

import batalha.naval.controlador.Controlador;
import batalha.naval.swing.Window;
import library.payload.comunicacao.EstadosMenu;
import library.payload.comunicacao.Mensagem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;


public class Cliente {
    private Socket socket;
    private ObjectOutputStream objectOuputStream;
    private ObjectInputStream objectInputStream;
    private Window window;
    private String ID;

    private String host;
    private int port;

    public Cliente(String host, int port, Window window, String ID){
        this.host = host;
        this.port = port;
        try{
            socket=new Socket(host,port);
            this.ID = ID;
            objectOuputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.window = window;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean reconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            socket = new Socket(host, port);
            objectOuputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            
            sendInput(new Mensagem(ID));
            return true;
        } catch (IOException e) {
            // e.printStackTrace();
            return false;
        }
    }

    private static String gerarIdUnico(){
        return LocalDate.now().toString();
    }

    public void sendInput(Object input){
        try{
            //objectOuputStream.flush();
            objectOuputStream.writeObject(input);
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

    public void find(){

    }
}
