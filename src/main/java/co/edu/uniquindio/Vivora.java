package co.edu.uniquindio;

import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import static co.edu.uniquindio.Tablero.*;

public class Vivora extends Thread {
    Nodo cabeza;
    String direccion;
    boolean controladaPorUsuario;
    Color color;
    boolean viva = true; // Indicates if the snake is alive
    int velocidad = 200; // Initial speed in milliseconds
    String nombre;
    static final int SIZE = 20; // Tamaño de la serpiente

    public Vivora(boolean controladaPorUsuario, int startX, int startY, String nombre) {
        this.controladaPorUsuario = controladaPorUsuario;
        this.cabeza = new Nodo(startX, startY);
        this.direccion = "derecha"; // Initial direction
        this.color = generarColorAleatorio();
        this.nombre = nombre;
    }

    // Generates a random color for the snake
    private Color generarColorAleatorio() {
        Random rand = new Random();
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setControladaPorUsuario(boolean controladaPorUsuario) {
        this.controladaPorUsuario = controladaPorUsuario;
    }

    @Override
public void run() {
    while (viva) {
        moverSnake();
        try {
            Thread.sleep(Tablero.velocidadGlobal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


    // Method to move the snake
    public synchronized void moverSnake() {
        int newX = cabeza.x;
        int newY = cabeza.y;

        if (!controladaPorUsuario) {
            cambiarDireccionAutomatica();
        }

        switch (direccion) {
            case "arriba":
                newY -= SIZE;
                break;
            case "abajo":
                newY += SIZE;
                break;
            case "izquierda":
                newX -= SIZE;
                break;
            case "derecha":
                newX += SIZE;
                break;
        }

        // Check for boundaries
        if (newX < LIMITE_IZQUIERDA || newY < LIMITE_ARRIBA || newX >= LIMITE_DERECHA || newY >= LIMITE_ABAJO) {
            viva = false;
            return;
        }

        cabeza.mover(newX, newY);
    }

    // Method to change direction automatically
    private void cambiarDireccionAutomatica() {
        String[] direcciones = {"arriba", "abajo", "izquierda", "derecha"};
        String direccionAnterior = this.direccion;
        Random rand = new Random();

        do {
            this.direccion = direcciones[rand.nextInt(4)];
        } while ((direccionAnterior.equals("arriba") && this.direccion.equals("abajo")) ||
                (direccionAnterior.equals("abajo") && this.direccion.equals("arriba")) ||
                (direccionAnterior.equals("izquierda") && this.direccion.equals("derecha")) ||
                (direccionAnterior.equals("derecha") && this.direccion.equals("izquierda")));
    }

    // Method to add a new node when the snake eats
    public synchronized void agregarNodo() {
        Nodo cola = cabeza.getCola();
        Nodo nuevoNodo = new Nodo(cola.prevX, cola.prevY);
        cola.siguienteNodo = nuevoNodo;
        // Aumentar la velocidad ligeramente
        if (velocidad > 80) {
            velocidad -= 20;
        }
    }


    public synchronized Nodo getCabeza() {
        return cabeza;
    }

    // Method to draw the snake
    public void draw(Graphics g) {
        Nodo current = cabeza;
        g.setColor(color);
        while (current != null) {
            g.fillOval(current.x, current.y, SIZE, SIZE); // Adjusted size
            g.setColor(Color.BLACK);
            g.drawOval(current.x, current.y, SIZE, SIZE); // Outline for better visibility
            current = current.siguienteNodo;
            g.setColor(color);
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}

