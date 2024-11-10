package co.edu.uniquindio;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tablero extends JPanel {
    List<Vivora> vivoras;
    CopyOnWriteArrayList<NodoComida> nodosComida = new CopyOnWriteArrayList<>();
    protected static final int LIMITE_IZQUIERDA = 20;
    protected static final int LIMITE_DERECHA = 780;
    protected static final int LIMITE_ARRIBA = 20;
    protected static final int LIMITE_ABAJO = 580;
    public static int velocidadGlobal = 200; // Usada por todas las víboras
    public static boolean gameActive;


    public Tablero(List<Vivora> vivoras) {
        this.vivoras = vivoras;
        setLayout(null); // Use absolute positioning for the game area
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(LIMITE_DERECHA -LIMITE_IZQUIERDA, LIMITE_ABAJO- LIMITE_ARRIBA));

        // Add key listener for controlling the active snake
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

        // Generate initial food
        generarComida();
    }

    public void generarComida() {
        nodosComida.add(new NodoComida());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw margins
        g.setColor(Color.GREEN);
        g.drawRect(LIMITE_IZQUIERDA, LIMITE_ARRIBA, LIMITE_DERECHA - LIMITE_IZQUIERDA, LIMITE_ABAJO - LIMITE_ARRIBA);

        for (Vivora vivora : vivoras) {
            if (vivora.viva) {
                vivora.draw(g);
            }
        }
        for (NodoComida comida : nodosComida) {
            comida.draw(g);
        }
    }

    // Method to update the game state
    public void actualizarJuego() {
        for (Vivora vivora : vivoras) {
            if (!vivora.viva) {
                continue;
            }

            synchronized (vivora) {
                // Obtener la cabeza de manera segura
                Nodo cabeza = vivora.getCabeza();
                Nodo current = cabeza.siguienteNodo;
                while (current != null) {
                    if (cabeza.x == current.x && cabeza.y == current.y) {
                        vivora.viva = false;
                        break;
                    }
                    current = current.siguienteNodo;
                }

                // Comprobación de colisión con comida
                for (NodoComida comida : nodosComida) {
                    if (cabeza.x == comida.coordenadaX && cabeza.y == comida.coordenadaY) {
                        vivora.agregarNodo();
                        nodosComida.remove(comida);
                        generarComida();
                        break;
                    }
                }

                // Comprobación de colisiones con otras víboras
                for (Vivora otherVivora : vivoras) {
                    if (otherVivora != vivora && otherVivora.viva) {
                        Nodo otherCurrent = otherVivora.getCabeza();
                        synchronized (otherVivora) {
                            while (otherCurrent != null) {
                                if (cabeza.x == otherCurrent.x && cabeza.y == otherCurrent.y) {
                                    vivora.viva = false;
                                    JOptionPane.showMessageDialog(this, "¡Colisión detectada! La víbora ha muerto.", 
                        "Colisión", JOptionPane.INFORMATION_MESSAGE);
                                    break;
                                }
                                otherCurrent = otherCurrent.siguienteNodo;
                            }
                        }
                    }
                }
            }
        }
        repaint();
    }
    //Posibles mejoras
    public void verificarFinDeJuego() {
        boolean todasMuertas = vivoras.stream().allMatch(vivora -> !vivora.viva);
        if (todasMuertas) {
            JOptionPane.showMessageDialog(this, "Juego Terminado", "Fin", JOptionPane.INFORMATION_MESSAGE);
            // Detener actualización del tablero
            gameActive = false;
        }
    }

    public void reiniciarPartida() {
        vivoras.clear();
        nodosComida.clear();
        generarComida(); // Agregar una comida inicial
        // Añadir una víbora inicial o implementar lógica para múltiples
        Vivora vivoraInicial = new Vivora(true, 200, 200, "Vívora 1");
        vivoras.add(vivoraInicial);
        vivoraInicial.start();
        gameActive = true; // Permitir actualizaciones del juego
        repaint();
    }
    
    
}





