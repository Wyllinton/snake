package co.edu.uniquindio;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;
//Clase que representa el área de juego extiende JPanel (el cual funciona como lienzo para actualizar la interfaz gráfica). Además controla la lógica de las víboras (Movimiento, colisiones y nodos de comida)
public class Tablero extends JPanel {
    List<Vivora> vivoras;
    CopyOnWriteArrayList<NodoComida> nodosComida = new CopyOnWriteArrayList<>();//Lista que permite la concurrencia entre varios hilos (viboras)
    protected static final int LIMITE_IZQUIERDA = 20;
    protected static final int LIMITE_DERECHA = 780;
    protected static final int LIMITE_ARRIBA = 20;
    protected static final int LIMITE_ABAJO = 580;
    public static int velocidadGlobal = 200; // Usada por todas las víboras

    public Tablero(List<Vivora> vivoras) {
        this.vivoras = vivoras;
        setLayout(null); //Posicionamiento Absoluto, el cual permite posicionar elementos con coordenadas específicas dentro del panel
        setBackground(Color.BLACK);
        setFocusable(true);//Gestiona la entrada de teclado
        setPreferredSize(new Dimension(LIMITE_DERECHA - LIMITE_IZQUIERDA, LIMITE_ABAJO - LIMITE_ARRIBA));

        //Se añade el key listener para las entradas de teclado, de acuerdo a la entrada se setea la dirección de la vibora 
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                for (Vivora vivora : vivoras) {
                    if (vivora.controladaPorUsuario) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_UP:
                                if (!vivora.getDireccion().equals("abajo"))
                                    vivora.setDireccion("arriba");
                                break;
                            case KeyEvent.VK_DOWN:
                                if (!vivora.getDireccion().equals("arriba"))
                                    vivora.setDireccion("abajo");
                                break;
                            case KeyEvent.VK_LEFT:
                                if (!vivora.getDireccion().equals("derecha"))
                                    vivora.setDireccion("izquierda");
                                break;
                            case KeyEvent.VK_RIGHT:
                                if (!vivora.getDireccion().equals("izquierda"))
                                    vivora.setDireccion("derecha");
                                break;
                        }
                    }
                }
            }
        });

        //Genera el primer nodo de comida
        generarComida();
    }

    public void generarComida() {
        nodosComida.add(new NodoComida());
    }

    //Método sobrescrito de JPanel que Swing llama automáticamente cada vez que el panel necesita actualizarse visualmente, aquí el JPanel se comporta como un lienzo que se actualiza constantemente de acuerdo a las diferentes ejecuciones 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Se dibuja los limites del área del juego
        g.setColor(Color.GREEN);
        g.drawRect(LIMITE_IZQUIERDA, LIMITE_ARRIBA, LIMITE_DERECHA - LIMITE_IZQUIERDA, LIMITE_ABAJO - LIMITE_ARRIBA);

        for (Vivora vivora : vivoras) {
            if (vivora.viva) {
                vivora.dibujarVibora(g);
            }
        }
        for (NodoComida comida : nodosComida) {
            comida.draw(g);
        }
    }

    //Método para actualizar el juego constantemente
    public void actualizarJuego() {
        for (Vivora vivora : vivoras) {
            if (!vivora.viva) {
                continue;
            }

            synchronized (vivora) {
                // Obtener la cabeza de manera segura
                Nodo cabeza = vivora.getCabeza();
                Nodo actual = cabeza.siguienteNodo;
                while (actual != null) {
                    if (cabeza.x == actual.x && cabeza.y == actual.y) {
                        vivora.viva = false;
                        break;
                    }
                    actual = actual.siguienteNodo;
                }

                for (NodoComida comida : nodosComida) {
                    // Ajusta el margen para verificar si la cabeza y la comida se alinean en el tablero
                    if (Math.abs(cabeza.x - comida.coordenadaX) < Nodo.TAMANO
                            && Math.abs(cabeza.y - comida.coordenadaY) < Nodo.TAMANO) {
                        //Se aumenta el tamaño de la vibora
                        vivora.agregarNodo();
                        nodosComida.remove(comida);
                        generarComida();
                        break;
                    }

                    // Comprobación de colisiones con otras víboras
                    for (Vivora viboraSiguiente : vivoras) {
                        if (viboraSiguiente != vivora && viboraSiguiente.viva) {
                            Nodo actualSiguiente = viboraSiguiente.getCabeza();
                            synchronized (viboraSiguiente) {
                                while (actualSiguiente != null) {
                                    if (cabeza.x == actualSiguiente.x && cabeza.y == actualSiguiente.y) {
                                        vivora.viva = false;
                                        JOptionPane.showMessageDialog(this, "¡Colisión detectada! La víbora ha muerto.",
                                                "Colisión", JOptionPane.INFORMATION_MESSAGE);
                                        break;
                                    }
                                    actualSiguiente = actualSiguiente.siguienteNodo;
                                }
                            }
                        }
                    }
                }
            }
            repaint();//Actualiza el lienzo 
        }
    }
}
