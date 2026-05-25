package batalha.naval.swing;

import javax.swing.*;
import java.awt.event.MouseListener;

public class Window extends JFrame {
    private Menu menu;
    private AreaJogo areaJogo;

    public Window(){
        super("Batalha Naval by David e Francisco");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 1100);

        menu = new Menu();
        add(menu);

        setFocusable(true);
        requestFocusInWindow();

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

    public void hideMenu(){
        remove(menu);
        windowUpdate();
    }

    public void showMenu(){
        add(menu);
        windowUpdate();
    }

    public void hideAreaJogo(){
        remove(areaJogo);
        windowUpdate();
    }

    public void showAreaJogo(){
        add(areaJogo);
        windowUpdate();
    }

    public void setAreaJogo(AreaJogo areaJogo){
        this.areaJogo = areaJogo;
    }

    public void moveUp(){
        areaJogo.moveUp();
    }

    public void moveDown(){
        areaJogo.moveDown();
    }

    public void moveLeft(){
        areaJogo.moveLeft();
    }

    public void moveRight(){
        areaJogo.moveRight();
    }

    public void setBarco(){
        areaJogo.setBarco();
    }

    public void rodarBarco(){
        areaJogo.rodarBarco();
    }

    public void appendLog(String log){
        areaJogo.appendLog(log);
        windowUpdate();
    }
}
