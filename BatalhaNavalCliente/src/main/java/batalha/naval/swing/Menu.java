package batalha.naval.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class Menu extends JPanel {
    MenuButton novoJogoButton;
    MenuButton carregarJogoButton;
    MenuButton sairButton;

    public Menu(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());
        novoJogoButton = new MenuButton("Novo Jogo");
        add(novoJogoButton);

        carregarJogoButton = new MenuButton("Carregar Jogo");
        add(carregarJogoButton);

        sairButton = new MenuButton("Sair");
        add(sairButton);

        add(Box.createVerticalGlue());
    }

    public void setNovoJogoButton(MouseListener e){
        novoJogoButton.addMouseListener(e);
    }

    public void setCarregarJogoButton(MouseListener e){
        carregarJogoButton.addMouseListener(e);
    }

    public void setSairButton(MouseListener e){
        sairButton.addMouseListener(e);
    }
}

class MenuButton extends JButton{
    public MenuButton(String string){
        super(string);
        setStyle();
    }

    private void setStyle(){
        Dimension dimension = new Dimension(185,100);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        setFont(new Font("Segoe UI Symbol", Font.BOLD, 30));
        setForeground(Color.gray);
        setContentAreaFilled(false);
        setFocusPainted(true);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}