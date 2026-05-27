package batalha.naval.swing;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import batalha.naval.cliente.Cliente;
import batalha.naval.controlador.Controlador;
import library.payload.barco.*;
import library.payload.comunicacao.EstadosMenu;
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
            areaMenu = new AreaMenu(cliente);
            add(areaMenu, c);
            c.gridy = 1;
            areaInimigo = new AreaInimigo(cliente);
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

            cliente.sendInput(areaJogador.getTabuleiroJogador());
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

    public void updateAreaJogador(Tabuleiro tabuleiro){
        areaJogador.setTabuleiroJogador(tabuleiro);
        window.windowUpdate();
    }

    public void updateAreaInimigo(Tabuleiro tabuleiro){
        areaInimigo.setTabuleiroInimigo(tabuleiro);
        window.windowUpdate();
    }

}

class AreaMenu extends JPanel{
    private JButton guardarJogoButton;
    private JButton desconectarButton;
    private Cliente cliente;

    public AreaMenu(Cliente cliente) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createHorizontalGlue());
        guardarJogoButton = new JButton("Guardar");
        setButtonStyle(guardarJogoButton);
        add(guardarJogoButton);
        guardarJogoButton.addMouseListener(new GuardarJogoButton(cliente));

        add(Box.createHorizontalStrut(20));

        desconectarButton = new JButton("Desconectar");
        setButtonStyle(desconectarButton);
        add(desconectarButton);

        add(Box.createHorizontalGlue());

        this.cliente=cliente;
    }

    public void setGuardarJogoButton(MouseListener e){guardarJogoButton.addMouseListener(e);}

    public void setDesconectarButton(MouseListener e){desconectarButton.addMouseListener(e);}

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
    private Cliente cliente;

    public AreaInimigo(Cliente cliente){
        setLayout(new GridLayout(10, 10, 2, 2));

        this.cliente = cliente;

        tabuleiroInimigo = new Tabuleiro();
        tabuleiroInimigo.setTabuleiroTipoTiro(true);

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                Color cor = tabuleiroInimigo.getEstadosTabuleiro(i,j).getColor();
                Casa casa = new Casa(i,j,cor);
                casa.addCasaMouseAdaptar(new CasaInimigoMouseAdaptar(cliente));
                add(casa);
            }
        }
    }

    public Tabuleiro getTabuleiroInimigo(){
        return tabuleiroInimigo;
    }

    public void setTabuleiroInimigo(Tabuleiro tabuleiro){
        tabuleiroInimigo = tabuleiro;
        updateViewFromTabuleiro();
    }

    public void updateViewFromTabuleiro(){
        for(Component component : getComponents()){
            if(component instanceof Casa){
                Casa casa = (Casa) component;
                EstadosTabuleiro getEstadosTabuleiro = tabuleiroInimigo.getEstadosTabuleiro(casa.getI(),casa.getJ());
                casa.setColor(getEstadosTabuleiro.getColor());
            }
        }
    }
}

class AreaJogador extends JPanel{
    private Tabuleiro tabuleiroJogador;

    public AreaJogador(){
        setLayout(new GridLayout(10, 10, 2, 2));

        tabuleiroJogador = new Tabuleiro();

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                Color cor = tabuleiroJogador.getEstadosTabuleiro(i,j).getColor();
                Casa casa = new Casa(i,j,cor);
                casa.addMouseListener(new CasaMouseAdaptar());
                add(casa);
            }
        }
    }

    public Tabuleiro getTabuleiroJogador(){
        return tabuleiroJogador;
    }

    public void setTabuleiroJogador(Tabuleiro tabuleiro){
        tabuleiroJogador = tabuleiro;
        updateViewFromTabuleiro();
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

    public void addCasaMouseAdaptar(CasaInimigoMouseAdaptar casaMouseAdaptar){
        this.addMouseListener(casaMouseAdaptar);
    }

    private void setStyle(){
        Dimension dimension = new Dimension(30,30);
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

class CasaInimigoMouseAdaptar implements MouseListener{
    private Cliente cliente;

    public CasaInimigoMouseAdaptar(Cliente cliente){
        this.cliente=cliente;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Component component = e.getComponent();
        if(component instanceof Casa){
            Casa casa = (Casa) component;

            Posicao posicao = new Posicao(casa.getI(),casa.getJ());
            cliente.sendInput(posicao);
        }

        while(component != null){
            component = component.getParent();
            if(component instanceof JFrame)
                ((JFrame)component).requestFocusInWindow();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Component component = e.getComponent();

        while(component != null){
            component = component.getParent();
            if(component instanceof JFrame)
                ((JFrame)component).requestFocusInWindow();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

class CasaMouseAdaptar implements MouseListener{

    @Override
    public void mouseClicked(MouseEvent e) {
        Component component = e.getComponent();

        while(component != null){
            component = component.getParent();
            if(component instanceof JFrame)
                ((JFrame)component).requestFocusInWindow();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Component component = e.getComponent();

        while(component != null){
            component = component.getParent();
            if(component instanceof JFrame)
                ((JFrame)component).requestFocusInWindow();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}

class  GuardarJogoButton implements MouseListener{
    private Cliente cliente;

    public GuardarJogoButton(Cliente cliente){
        this.cliente=cliente;
        System.out.println("Criou!");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Clicou!");
        cliente.sendInput(EstadosMenu.GUARDAR);
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
