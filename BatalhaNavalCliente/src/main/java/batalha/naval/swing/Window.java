package batalha.naval.swing;

import javax.swing.*;
import java.awt.event.MouseListener;

public class Window extends JFrame {
    private Menu menu;

    public Window(){
        super("Batalha Naval by David e Francisco");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 1100);

        menu = new Menu();
        //add(menu);
        //setLayout(null);
        AreaJogo areaJogo = new AreaJogo();
        add(areaJogo);

        setVisible(true);
    }

    public void setNovoJogoButton(MouseListener e){
        menu.setNovoJogoButton(e);
    }

    public void setCarregarJogoButton(MouseListener e){
        menu.setCarregarJogoButton(e);
    }

    public void setSairButton(MouseListener e){
        menu.setSairButton(e);
    }

    public void windowUpdate(){
        revalidate();
        repaint();
    }
}
