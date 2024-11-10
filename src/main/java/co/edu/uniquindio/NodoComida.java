package co.edu.uniquindio;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import static co.edu.uniquindio.Tablero.*;

public class NodoComida {
    int coordenadaX, coordenadaY;
    Color color;
    static final int TAMANO = 20; // Tamaño de la comida

    public NodoComida() {
        Random rand = new Random();
        this.coordenadaX = LIMITE_IZQUIERDA + rand.nextInt((LIMITE_DERECHA - LIMITE_IZQUIERDA) / TAMANO) * TAMANO;
        this.coordenadaY = LIMITE_ARRIBA + rand.nextInt((LIMITE_ABAJO - LIMITE_ARRIBA) / TAMANO) * TAMANO;
        this.color = Color.PINK;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(coordenadaX, coordenadaY, TAMANO, TAMANO);
    }
}
