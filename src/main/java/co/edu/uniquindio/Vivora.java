package co.edu.uniquindio;

import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import static co.edu.uniquindio.Tablero.*;

//Esta clase representa cada vivora instanciada, es un hilo, puede moverse por controles o de manera aleatoria y usa los nodos como cuerpo
public class Vivora extends Thread {
    Nodo cabeza;
    String direccion;// Condición de que la dirección sea un String
    boolean controladaPorUsuario;// Variable para representar si el usuario tiene el control
    Color color; // Componente de graphics
    boolean viva = true; // Variable que establece si la vibora se encuentra "viva"
    int velocidad = 200; // Velocidad inicial de las viboras
    String nombre; // Nombre de identificación para cada vibora
    static final int SIZE = Nodo.TAMANO; // Tamaño de la serpiente

    public Vivora(boolean controladaPorUsuario, int startX, int startY, String nombre) {
        this.controladaPorUsuario = controladaPorUsuario;
        this.cabeza = new Nodo(startX, startY);
        this.direccion = "abajo"; // Initial direction
        this.color = generarColorAleatorio();
        this.nombre = nombre;
    }

    // Generates a random color for the snake
    private Color generarColorAleatorio() {
        Random rand = new Random();
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    // Dirección en formato string (requisito)
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setControladaPorUsuario(boolean controladaPorUsuario) {
        this.controladaPorUsuario = controladaPorUsuario;
    }

    //Hilo que utilizara cada vibora, tiene un bucle que se ejecutara siempre si está se encuentra viva, además se setea la velocidad del juego (dificultad)
    @Override
    public void run() {
        while (viva) {
            moverVibora();
            try {
                Thread.sleep(Tablero.velocidadGlobal);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Mueve la vibora, según el string que recibe se modifica las coordenadas en X, Y
    public synchronized void moverVibora() {
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

        //Verifica que las coordenadas se encuentren en el tablero de juego
        if (newX < LIMITE_IZQUIERDA || newY < LIMITE_ARRIBA || newX >= LIMITE_DERECHA || newY >= LIMITE_ABAJO) {
            viva = false; //Si se encuentra con alguno de estos limites, la vibora "muere"
            return;
        }

        cabeza.moverNodo(newX, newY);
    }

    //Método para mover la vibora de forma automatica, se define un array de direcciones, luego de forma aleatoria se generan direcciones que serán seteadas a la vibora
    private void cambiarDireccionAutomatica() {
        String[] direcciones = { "arriba", "abajo", "izquierda", "derecha" };
        String direccionAnterior = this.direccion;
        Random rand = new Random();

        do {
            this.direccion = direcciones[rand.nextInt(4)];
        } while ((direccionAnterior.equals("arriba") && this.direccion.equals("abajo")) ||
                (direccionAnterior.equals("abajo") && this.direccion.equals("arriba")) ||
                (direccionAnterior.equals("izquierda") && this.direccion.equals("derecha")) ||
                (direccionAnterior.equals("derecha") && this.direccion.equals("izquierda")));
    }

    //Método que simula el crecimiento de la vibora 
    public synchronized void agregarNodo() {
        Nodo cola = cabeza.getCola();
        Nodo nuevoNodo = new Nodo(cola.prevX, cola.prevY);
        cola.siguienteNodo = nuevoNodo;
        // Aumentar la velocidad cada vez que come para aumentar la dificultad de manera sútil con limite de 80
        if (velocidad > 80) {
            velocidad -= 20;
        }
    }

    public synchronized Nodo getCabeza() {
        return cabeza;
    }

    //Método para dibujar una vibora en el tablero de juego usando la librería Java Graphics 
    public void dibujarVibora(Graphics g) {
        Nodo actual = cabeza;
        g.setColor(color);
        while (actual != null) {
            g.fillOval(actual.x, actual.y, SIZE, SIZE); //Método para hacer un ovalo según paramteros de tamaño y coordenadas
            g.setColor(Color.WHITE);
            g.drawOval(actual.x, actual.y, SIZE, SIZE); // Dibujar un contorno 
            actual = actual.siguienteNodo;
            g.setColor(color);
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}
