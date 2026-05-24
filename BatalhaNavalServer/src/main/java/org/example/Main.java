package org.example;

import batalha.naval.server.Servidor;

public class Main {
    static void main() {
        Servidor servidor = new Servidor(8080);
        servidor.run();
    }
}
