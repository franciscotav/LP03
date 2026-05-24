package batalha.naval.swing;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class AreaJogo extends JPanel{
    private AreaMenu areaMenu;
    private AreaJogador areaJogador;
    private AreaInimigo areaInimigo;
    private AreaLogs areaLogs;

    public AreaJogo(){
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.anchor = GridBagConstraints.CENTER;

            c.gridy = 0;
            areaMenu = new AreaMenu();
            add(areaMenu, c);
            c.gridy = 1;
            areaInimigo = new AreaInimigo();
            add(areaInimigo,c);
            c.gridy = 2;
            c.insets = new Insets(10, 0, 0, 0);
            areaJogador = new AreaJogador();
            add(areaJogador,c);
            c.gridy = 3;
            c.weighty = 1.0;
            c.weightx = 1.0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(10, 10, 10, 10);
            areaLogs = new AreaLogs();
            add(areaLogs,c);
    }

    public void appendLog(String log){
        areaLogs.appendLog(log);
    }

}

class AreaMenu extends JPanel{
    private JButton guardarJogoButton;
    private JButton desconectarButton;
    public AreaMenu() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createHorizontalGlue());
        guardarJogoButton = new JButton("Guardar");
        setButtonStyle(guardarJogoButton);
        add(guardarJogoButton);
        add(Box.createHorizontalStrut(20));

        desconectarButton = new JButton("Desconectar");
        setButtonStyle(desconectarButton);
        add(desconectarButton);

        add(Box.createHorizontalGlue());
    }

    private void setButtonStyle(JButton jButton){
        jButton.setFont(new Font("Segoe UI Symbol", Font.BOLD, 25));
        jButton.setFocusPainted(false);
        jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        jButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        jButton.setForeground(Color.gray);
        jButton.setContentAreaFilled(false);
        jButton.setFocusPainted(true);
        jButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}

class AreaLogs extends JScrollPane{
    private JTextArea logAreaTexto;

    public AreaLogs(){
        logAreaTexto = new JTextArea();
        logAreaTexto.setEditable(false);
        logAreaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logAreaTexto.append("Logs..\n");
        logAreaTexto.setLineWrap(true);
        logAreaTexto.setWrapStyleWord(true);

        setViewportView(logAreaTexto);
        DefaultCaret caret = (DefaultCaret) logAreaTexto.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public void appendLog(String log){
        logAreaTexto.append(log + "\n");
    }
}

class AreaInimigo extends JPanel{
    public AreaInimigo(){
        setLayout(new GridLayout(10, 10, 2, 2));

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Casa casa = new Casa(x,y);
                add(casa);
            }
        }
    }
}

class AreaJogador extends JPanel{
    public AreaJogador(){
        setLayout(new GridLayout(10, 10, 2, 2));

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Casa casa = new Casa(x,y);
                add(casa);
            }
        }
    }
}

class Casa extends JButton{
    private int x;
    private int y;

    public Casa(int x, int y){
        setStyle();
    }

    private void setStyle(){
        Dimension dimension = new Dimension(40,40);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setAlignmentY(Component.CENTER_ALIGNMENT);

        //setFont(new Font("Segoe UI Symbol", Font.BOLD, 30));
        setBackground(new Color(0,255,255));
        setContentAreaFilled(true);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }


}