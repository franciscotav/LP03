package batalha.naval.server;

import library.payload.comunicacao.Mensagem;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class Servidor {
    private static int numeroJogos;
    private static int numeroJogadores;
    private LinkedBlockingDeque<BNJogo> bnJogos;
    private BNServerSocket bnServerSocket;

    private Map<String, BNJogador> jogadoresAtivos = new ConcurrentHashMap<>();
    private long tempoEspera = 30000; //30 segundos

    public Servidor(int porta) {
        try {
            numeroJogos = 0;
            numeroJogadores = 0;
            bnJogos = new LinkedBlockingDeque<>();

            bnServerSocket = new BNServerSocket(porta);
            Thread listeningThread = new Thread(bnServerSocket);
            listeningThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Thread thread = new Thread(new VerifcarJogadores());
        thread.start();

        while (true) {
            try {
                Socket socket = bnServerSocket.clients().take();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                Object input = in.readObject();

                if (!(input instanceof Mensagem)) {
                    continue;
                }

                Mensagem mensagem = (Mensagem) input;
                String idConecao = mensagem.getMensagem();

                if (idConecao == null) {
                    continue;
                }

                System.out.println("Connection ID: " + idConecao);

                if (jogadoresAtivos.containsKey(idConecao)) {
                    BNJogador p = jogadoresAtivos.get(idConecao);
                    if (p.isDesconetado()) {
                        p.setDesconetado(false);
                        p.setSocket(socket, out, in);
                        
                        Thread jogadorThread = new Thread(p);
                        jogadorThread.start();
                    }
                } else {
                    BNJogador bnJogador = new BNJogador(socket, this, criarPlayerID(), idConecao, out, in);
                    jogadoresAtivos.put(idConecao, bnJogador);

                    Thread jogadorThread = new Thread(bnJogador);
                    jogadorThread.start();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }catch(ClassNotFoundException e){
                e.printStackTrace();
                break;
            }
        }
    }

    public LinkedBlockingDeque<BNJogo> getBNJogos() {
        return bnJogos;
    }

    public BNJogo getBNJogo(String idJogo) {
        for (BNJogo bnJogo : bnJogos) {
            if (bnJogo.getJogoId().equals(idJogo)) {
                return bnJogo;
            }
        }
        return null;
    }

    public void addBNJogo(BNJogo bnJogo) {
        bnJogos.add(bnJogo);
    }

    public BNJogo getBNJogoDisponivel() {
        for (BNJogo bnJogo : bnJogos) {
            String[] id = bnJogo.getJogoId().split("_");
            if (id[id.length - 1].equals("carregado"))
                continue;

            if (bnJogo.esperaJogador()) {
                return bnJogo;
            }
        }

        return null;
    }

    public synchronized String criarJogoID() {
        numeroJogos++;
        return "GAME%d".formatted(numeroJogos);
    }

    public synchronized String criarPlayerID() {
        numeroJogadores++;
        return "PLAYER%d".formatted(numeroJogadores);
    }

    private class VerifcarJogadores implements Runnable {
        public void run() {
            while (true) {
                verificarTempoEspera();
            }
        }

        public void verificarTempoEspera() {
            long agora = System.currentTimeMillis();
            jogadoresAtivos.entrySet().removeIf(entry -> {
                BNJogador p = entry.getValue();
                boolean timeout = p.isDesconetado() && (agora - p.getTempoDesconetado() > tempoEspera);
                if (timeout) {
                    BNJogo jogo = p.getBnJogo();
                    if (jogo != null) {
                        jogo.vitoriaPorDesistencia(p);
                    }
                }
                return timeout;
            });
        }

    }
}