package co.edu.uniquindio;

public class Nodo {
    int x, y;
    int prevX, prevY;
    Nodo siguienteNodo = null;

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

    //Método recursivo de movimiento de los nodos 
    public void mover(int nuevoX, int nuevoY) {
        prevX = x;
        prevY = y;
        x = nuevoX;
        y = nuevoY;
        if (siguienteNodo != null) {
            siguienteNodo.mover(prevX, prevY);
        }
    }

    // Método para obtener la cola
    public Nodo getCola() {
        if (siguienteNodo == null) {
            return this;
        } else {
            return siguienteNodo.getCola();
        }
    }
}

