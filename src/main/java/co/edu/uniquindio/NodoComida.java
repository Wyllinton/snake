package co.edu.uniquindio;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import static co.edu.uniquindio.Tablero.*;
//Clase que representa la "comida" de la vibora con un nodo, que aparecerá en el tablero de juego de manera aleatoria
public class NodoComida {
    int coordenadaX, coordenadaY;
    Color color;
    static final int TAMANO = Nodo.TAMANO; // Tamaño de la comida

    public NodoComida() {
        Random rand = new Random();
        // Retorna una coordenada en X de forma aleatoria dentro de los límites horizontales y alineada al tamaño del nodo, esto último con el fin de facilitar las detecciones de colisión
        this.coordenadaX = LIMITE_IZQUIERDA + rand.nextInt((LIMITE_DERECHA - LIMITE_IZQUIERDA) / TAMANO) * TAMANO;
        // Retorna una coordenada en Y de forma aleatoria dentro de los límites verticales e igualmente alineada
        this.coordenadaY = LIMITE_ARRIBA + rand.nextInt((LIMITE_ABAJO - LIMITE_ARRIBA) / TAMANO) * TAMANO;

        this.color = Color.PINK;//Define el color del nodo comida
    }
    /*Configura el color, luego dibuja un óvalo en las coordenadas de X, Y con el tamaño determinado por `TAMANO` (ancho y alto del ovalo en este caso 20, 20).*/
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(coordenadaX, coordenadaY, TAMANO, TAMANO);
    }
}
