package batalha.naval.controlador;

import batalha.naval.cliente.Cliente;
import batalha.naval.swing.AreaJogo;
import batalha.naval.swing.Window;
import library.payload.comunicacao.EstadosMenu;
import library.payload.comunicacao.Mensagem;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.Date;

public class Controlador {
    private Window window;
    private Cliente cliente;
    private String defaultIP;
    private String ultimoIdJogo;
    private boolean ultimoJogadorSelecionado;
    private String ID;
    private LogicaJogo logicaJogo;

    public Controlador(){
        window = new Window();
        cliente = null;
        defaultIP = "127.0.0.1:8080";
        ID = Long.toString(System.nanoTime());
        init();
    }

    private void init(){
        window.setNovoJogoButton(new NovoJogoMouseAdapter());
        window.setCarregarJogoButton(new CarregarJogoButton());
        window.setSairButton(new SairButton());
    }

    private class SairButton implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            System.exit(0);
        }

        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }

    private class DesconectarButton implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (logicaJogo != null) {
                logicaJogo.stop();
            }
            cliente.sendInput(EstadosMenu.QUIT);
            window.hideAreaJogo();
            window.showMenu();
        }

        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }

    private class NovoJogoMouseAdapter implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(!enterServerInformation()) return;

            String host = defaultIP.split(":")[0];
            int port = Integer.parseInt(defaultIP.split(":")[1]);
            cliente = new Cliente(host,port, window, ID);

            cliente.sendInput(new Mensagem(ID));
            cliente.sendInput(EstadosMenu.NOVO_JOGO);


            window.hideMenu();

            AreaJogo areaJogo = new AreaJogo(window,cliente);
            window.setAreaJogo(areaJogo);
            window.showAreaJogo();
            window.setDesconectarButton(new DesconectarButton());
            window.addKeyListener(new Controlador.JogoKeyAdapter());

            logicaJogo = new LogicaJogo(window,cliente);
            Thread logicaJogothread = new Thread(logicaJogo);
            logicaJogothread.start();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    private class CarregarJogoButton implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if(!enterServerInformationCarregarJogo()) return;

            String host = defaultIP.split(":")[0];
            int port = Integer.parseInt(defaultIP.split(":")[1]);
            cliente = new Cliente(host,port, window, ID);

            cliente.sendInput(new Mensagem(ID));
            cliente.sendInput(EstadosMenu.CARREGAR_JOGO);
            cliente.sendInput(new Mensagem(ultimoIdJogo));
            cliente.sendInput(ultimoJogadorSelecionado);

            AreaJogo areaJogo = new AreaJogo(window,cliente);

            window.hideMenu();
            window.setAreaJogo(areaJogo);
            window.showAreaJogo();
            window.setDesconectarButton(new DesconectarButton());
            window.addKeyListener(new Controlador.JogoKeyAdapter());

            logicaJogo = new LogicaJogo(window,cliente);
            Thread logicaJogothread = new Thread(logicaJogo);
            logicaJogothread.start();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }


    private boolean enterServerInformation(){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        JTextField fieldTitulo = new JTextField(defaultIP);
        jPanel.add(fieldTitulo);

        int result = JOptionPane.showConfirmDialog(window, jPanel,
                "IP:PORT", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION){
            defaultIP = fieldTitulo.getText();
            return true;
        }

        return false;
    }

    private boolean enterServerInformationCarregarJogo(){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        JTextField fieldTitulo = new JTextField(defaultIP);
        JLabel labelIdjogo = new JLabel("Insire ID do jogo: ");
        JTextField fieldIdJogo = new JTextField();

        JRadioButton botaoA = new JRadioButton("Jogador A");
        JRadioButton botaoB = new JRadioButton("Jogador B");

        ButtonGroup botoesGrupo = new ButtonGroup();
        botoesGrupo.add(botaoA);
        botoesGrupo.add(botaoB);

        jPanel.add(fieldTitulo);
        jPanel.add(labelIdjogo);
        jPanel.add(fieldIdJogo);
        jPanel.add(botaoA);
        jPanel.add(botaoB);

        int result = JOptionPane.showConfirmDialog(window, jPanel,
                "Carregar Jogo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION){
            ultimoIdJogo=fieldIdJogo.getText();
            defaultIP = fieldTitulo.getText();
            if(botaoB.isSelected()){
                ultimoJogadorSelecionado = true;
            }else{
                ultimoJogadorSelecionado = false;
            }
            return true;
        }

        return false;
    }

    public String getUltimoIdJogo(){
        return ultimoIdJogo;
    }

    public boolean getUltimoJogadorSelecionado(){
        return ultimoJogadorSelecionado;
    }

    private class JogoKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e){
            int keyCode = e.getKeyCode();

            if(keyCode == KeyEvent.VK_UP){
                window.moveUp();
                //window.appendLog("Pressed UP");
            }

            if(keyCode == KeyEvent.VK_LEFT){
                window.moveLeft();
                //window.appendLog("Pressed LEFT");
            }

            if(keyCode == KeyEvent.VK_RIGHT){
                window.moveRight();
                //window.appendLog("Pressed RIGHT");
            }

            if(keyCode == KeyEvent.VK_DOWN){
                window.moveDown();
                //window.appendLog("Pressed DOWN");
            }

            if(keyCode == KeyEvent.VK_SHIFT){
                window.rodarBarco();
            }

            if(keyCode == KeyEvent.VK_ENTER){
                window.setBarco();
            }

        }
    }
}

