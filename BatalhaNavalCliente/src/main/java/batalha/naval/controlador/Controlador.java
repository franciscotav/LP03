package batalha.naval.controlador;

import batalha.naval.cliente.Cliente;
import batalha.naval.swing.AreaJogo;
import batalha.naval.swing.Window;
import library.payload.comunicacao.EstadosMenu;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Controlador {
    private Window window;
    private Cliente cliente;
    private String defaultIP;

    public Controlador(){
        window = new Window();
        cliente = null;
        defaultIP = "127.0.0.1:8080";
        init();
    }

    private void init(){
        window.setNovoJogoButton(new NovoJogoMouseAdapter());
    }

    class NovoJogoMouseAdapter implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(!enterServerInformation()) return;

            String host = defaultIP.split(":")[0];
            int port = Integer.parseInt(defaultIP.split(":")[1]);
            cliente = new Cliente(host,port);

            cliente.sendInput(EstadosMenu.NOVO_JOGO);

            window.hideMenu();

            AreaJogo areaJogo = new AreaJogo();
            window.setAreaJogo(areaJogo);
            window.showAreaJogo();

            LogicaJogo logicaJogo = new LogicaJogo(window,cliente);
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
}

