package org.example;

import batalha.naval.servidor.Servidor;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        Servidor servidor = new Servidor(8080);
        servidor.listening();
    }
}
