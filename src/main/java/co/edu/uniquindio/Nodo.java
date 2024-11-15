package co.edu.uniquindio;

//Clase que se utilizara para la cabeza y cuerpo de la vibora, contiene lógica interna para manejar el sistema de coordenadas en los ejes X, Y
public class Nodo {
    int x, y;
    int prevX, prevY;
    Nodo siguienteNodo = null;
    public static final int TAMANO = 20;    

    public Nodo(int x, int y) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
    }

    public int getX() { 
        return x; 
    }
    
    public int getY() { 
        return y; 
    }

    public Nodo getSiguienteNodo() { 
        return siguienteNodo; 
    }
    
    public void setSiguienteNodo(Nodo siguienteNodo) { 
        this.siguienteNodo = siguienteNodo; 
    }

    //Método recursivo de movimiento, usando las coordenadas del nodo 
    public void moverNodo(int nuevoX, int nuevoY) {
        prevX = x;
        prevY = y;
        x = nuevoX;
        y = nuevoY;
        if (siguienteNodo != null) {
            siguienteNodo.moverNodo(prevX, prevY);
        }
    }

    // Método para obtener la cola de la vibora
    public Nodo getCola() {
        if (siguienteNodo == null) {
            return this;
        } else {
            return siguienteNodo.getCola();
        }
    }
}

