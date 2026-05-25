package batalha.naval.swing;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyListener;

import batalha.naval.cliente.Cliente;
import library.payload.barco.*;
import library.payload.tabuleiro.EstadosTabuleiro;
import library.payload.tabuleiro.Posicao;
import library.payload.tabuleiro.Tabuleiro;

public class AreaJogo extends JPanel{
    private Window window;
    private Cliente cliente;

    private AreaMenu areaMenu;
    private AreaJogador areaJogador;
    private AreaInimigo areaInimigo;
    private AreaLogs areaLogs;

    private boolean areaJogadorSetup;
    private int xPosicaoSetup, yPosicaoSetup;
    private Barco barcoSetup;
    private Orientacao orientacaoSetup;

    public AreaJogo(Window window, Cliente cliente){
            this.window = window;
            this.cliente = cliente;

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

            xPosicaoSetup = 4;
            yPosicaoSetup = 4;
            orientacaoSetup = Orientacao.SUL;
            areaJogadorSetup = true;
            barcoSetup = new BarcoBote();
            areaJogador.moveBarco(xPosicaoSetup,yPosicaoSetup,barcoSetup,orientacaoSetup);
    }

    public void appendLog(String log){
        areaLogs.appendLog(log);
    }

    private void proximoBarcoSetup(){
        xPosicaoSetup = 4;
        yPosicaoSetup = 4;
        orientacaoSetup = Orientacao.SUL;
        areaJogadorSetup = true;

        if(barcoSetup instanceof BarcoBote)
            barcoSetup = new BarcoLancha();
        else if(barcoSetup instanceof BarcoLancha)
            barcoSetup = new BarcoPortaAvioes();
        else if(barcoSetup instanceof BarcoPortaAvioes)
            barcoSetup = new BarcoSubmarino();
        else if(barcoSetup instanceof BarcoSubmarino)
            barcoSetup = new BarcoVeleiro();
        else{
            barcoSetup = null;
            areaJogadorSetup = false;
        }

    }

    public void moveUp(){
        Boolean limites = areaJogador.verificaLimites(barcoSetup,new Posicao(xPosicaoSetup,yPosicaoSetup - 1), orientacaoSetup);
        if(!limites) return;
        areaJogador.updateViewFromTabuleiro();
        yPosicaoSetup--;
        areaJogador.moveBarco(xPosicaoSetup,yPosicaoSetup,barcoSetup,orientacaoSetup);
    }

    public void moveDown(){
        Boolean limites = areaJogador.verificaLimites(barcoSetup,new Posicao(xPosicaoSetup,yPosicaoSetup + 1), orientacaoSetup);
        if(!limites) return;
        areaJogador.updateViewFromTabuleiro();
        yPosicaoSetup++;
        areaJogador.moveBarco(xPosicaoSetup,yPosicaoSetup,barcoSetup,orientacaoSetup);
    }

    public void moveLeft(){
        Boolean limites = areaJogador.verificaLimites(barcoSetup,new Posicao(xPosicaoSetup - 1,yPosicaoSetup), orientacaoSetup);
        if(!limites) return;
        areaJogador.updateViewFromTabuleiro();
        xPosicaoSetup--;
        areaJogador.moveBarco(xPosicaoSetup,yPosicaoSetup,barcoSetup,orientacaoSetup);
    }

    public void moveRight(){
        Boolean limites = areaJogador.verificaLimites(barcoSetup,new Posicao(xPosicaoSetup + 1,yPosicaoSetup), orientacaoSetup);
        if(!limites) return;
        areaJogador.updateViewFromTabuleiro();
        xPosicaoSetup++;
        areaJogador.moveBarco(xPosicaoSetup,yPosicaoSetup,barcoSetup,orientacaoSetup);
    }

    public void setBarco(){
        Boolean limites = areaJogador.updateTabuleiro(barcoSetup,new Posicao(xPosicaoSetup,yPosicaoSetup),orientacaoSetup);
        if(!limites){
            areaLogs.appendLog("Barco " + barcoSetup.getTipo().toString()
                    + " (" + xPosicaoSetup + "," + yPosicaoSetup + ")" + " posição invalida");
            return;
        }

        proximoBarcoSetup();
        areaJogador.updateViewFromTabuleiro();

        if(areaJogadorSetup){
            areaJogador.moveBarco(xPosicaoSetup,yPosicaoSetup,barcoSetup,orientacaoSetup);
        }else{
            KeyListener[] keyListeners = window.getKeyListeners();
            for(KeyListener keyListener : keyListeners){
                window.removeKeyListener(keyListener);
            }

            cliente.sendInput(areaJogador.tabuleiroJogador);
            areaLogs.appendLog("Enviou barcos para servidor");
        }

    }

    public void rodarBarco(){
        Orientacao atual = orientacaoSetup;
        boolean conseguiuGirar = false;

        for(int i = 0; i < 4; i++){
            Orientacao proxima;
            switch(atual){
                case Orientacao.SUL:   proxima = Orientacao.OESTE; break;
                case Orientacao.OESTE: proxima = Orientacao.NORTE; break;
                case Orientacao.NORTE: proxima = Orientacao.ESTE; break;
                case Orientacao.ESTE:  proxima = Orientacao.SUL; break;
                default: proxima = atual;
            }

            if(areaJogador.verificaLimites(barcoSetup, new Posicao(xPosicaoSetup, yPosicaoSetup), proxima)){
                orientacaoSetup = proxima;
                areaJogador.updateViewFromTabuleiro();
                areaJogador.moveBarco(xPosicaoSetup, yPosicaoSetup, barcoSetup, orientacaoSetup);
                conseguiuGirar = true;
                break;
            }

            atual = proxima;
        }

        if(!conseguiuGirar){
            areaLogs.appendLog("Barco " + barcoSetup.getTipo().toString()
                    + " (" + xPosicaoSetup + "," + yPosicaoSetup + ") "
                    + "não consgue girar.");
        }
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
    private Tabuleiro tabuleiroInimigo;

    public AreaInimigo(){
        setLayout(new GridLayout(10, 10, 2, 2));

        tabuleiroInimigo = new Tabuleiro();

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                Color cor = tabuleiroInimigo.getEstadosTabuleiro(i,j).getColor();
                Casa casa = new Casa(i,j,cor);
                add(casa);
            }
        }
    }
}

class AreaJogador extends JPanel{
    Tabuleiro tabuleiroJogador;

    public AreaJogador(){
        setLayout(new GridLayout(10, 10, 2, 2));

        tabuleiroJogador = new Tabuleiro();

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                Color cor = tabuleiroJogador.getEstadosTabuleiro(i,j).getColor();
                Casa casa = new Casa(i,j,cor);
                add(casa);
            }
        }
    }

    public Boolean verificaLimites(Barco barcoSetup, Posicao posicao, Orientacao orientacao){
        return tabuleiroJogador.verificaLimites(barcoSetup,posicao,orientacao);
    }

    public void updateViewFromTabuleiro(){

        for(Component component : getComponents()){
            if(component instanceof Casa){
                Casa casa = (Casa) component;
                EstadosTabuleiro getEstadosTabuleiro = tabuleiroJogador.getEstadosTabuleiro(casa.getI(),casa.getJ());
                casa.setColor(getEstadosTabuleiro.getColor());
            }
        }
    }

    public void updateComponents(int i, int j, EstadosTabuleiro estadosTabuleiro){

        for(Component component : getComponents()){
            if(component instanceof Casa){
                Casa casa = (Casa) component;
                if(casa.getI() == i && casa.getJ() == j){
                    casa.setColor(estadosTabuleiro.getColor());
                }
            }
        }
    }

    public boolean updateTabuleiro(Barco barco, Posicao posicao, Orientacao orientacao){
        return tabuleiroJogador.adicionarBarco(barco, posicao, orientacao);
    }

    public void moveBarco(int i, int j, Barco barco, Orientacao orientacao){
       switch(orientacao){
            case Orientacao.NORTE:
                updateComponents(i,j,barco.getTipo());
                break;
            case Orientacao.ESTE:
                updateComponents(i,j,barco.getTipo());
                break;
            case Orientacao.SUL:
                updateComponents(i,j,barco.getTipo());
                break;
            case Orientacao.OESTE:
                updateComponents(i,j,barco.getTipo());
                break;
        }

        for(int n = 1; n < barco.getComprimento(); n++){
            switch(orientacao){
                case Orientacao.SUL: updateComponents(i,j + n,barco.getTipo()); break;
                case Orientacao.OESTE: updateComponents(i - n, j,barco.getTipo()); break;
                case Orientacao.NORTE: updateComponents(i,j - n,barco.getTipo()); break;
                case Orientacao.ESTE: updateComponents(i + n, j,barco.getTipo());; break;
            }
        }

    }
}

class Casa extends JButton{
    private int i;
    private int j;
    private Color cor;

    public Casa(int i, int j, Color cor){
        this.i = i;
        this.j = j;
        this.cor = cor;

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
        setBackground(cor);
        setContentAreaFilled(true);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setColor(Color cor){
        this.cor = cor;
        setBackground(cor);
    }

    public int getI(){
        return i;
    }

    public int getJ(){
        return j;
    }
}