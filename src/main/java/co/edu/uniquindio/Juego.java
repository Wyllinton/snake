package co.edu.uniquindio;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/*Juego extiende JFrame funciona como la ventana principal del juego.
 * Define un panel lateral con controles adicionales para interactuar con el juego, como la adición de nuevas *víboras y la selección del nivel de dificultad.
*También maneja la lógica para actualizar el juego en intervalos definidos de tiempo. */

public class Juego extends JFrame {
    List<Vivora> vivoras;
    Tablero tablero;
    DefaultListModel<Vivora> viborasInterfaz;//Lista que mostrará las víboras en la interfaz
    Timer timer;

    public Juego() {
        setTitle("Snake");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        vivoras = new ArrayList<>();
        tablero = new Tablero(vivoras);

        // Panel lateral para seleccionar la vibora, lista de víboras 
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(250, getHeight()));

        //Botón para añadir una víbora
        JButton agregarVivoraButton = new JButton("Añadir Víbora");
        agregarVivoraButton.addActionListener(e -> agregarVivora());
        sidePanel.add(agregarVivoraButton, BorderLayout.SOUTH);

        // Lista de víboras
        viborasInterfaz = new DefaultListModel<>();
        JList<Vivora> listaViboras = new JList<>(viborasInterfaz);
        listaViboras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaViboras.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Vivora selectedVivora = listaViboras.getSelectedValue();
                if (selectedVivora != null) {
                    actualizarControl(selectedVivora);
                    tablero.requestFocusInWindow();
                }
            }
        });
        JScrollPane listaViborasScrollPane = new JScrollPane(listaViboras);
        sidePanel.add(listaViborasScrollPane, BorderLayout.CENTER);

        // Panel de dificultad
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new FlowLayout());
        
        JButton dificilBoton = new JButton("Fácil");
        dificilBoton.addActionListener(e -> cambiarDificultad(100)); // Velocidad rápida para difícil

        JButton normalBoton = new JButton("Difícil");
        normalBoton.addActionListener(e -> cambiarDificultad(50)); // Velocidad lenta para fácil

        difficultyPanel.add(normalBoton);
        difficultyPanel.add(dificilBoton);
        
        sidePanel.add(difficultyPanel, BorderLayout.NORTH); // Añade el panel de dificultad en la parte superior

        // Añade los paneles al marco principal
        add(tablero, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.WEST);

        // Crea la primera víbora controlada por el usuario
        agregarVivoraInicial();

        // Inicia el bucle del juego con el timer
        timer = new Timer(Tablero.velocidadGlobal, e -> actualizarJuego());
        timer.start();
    }

    private void agregarVivoraInicial() {
        String nombre = "Vívora 1";
        Vivora primeraVivora = new Vivora(true, 150, 175, nombre);
        vivoras.add(primeraVivora);
        viborasInterfaz.addElement(primeraVivora);
        actualizarControl(primeraVivora);
        primeraVivora.start();
    }

    private void agregarVivora() {
        String nombre = "Vívora " + (vivoras.size() + 1);
        Vivora nuevaVivora = new Vivora(true, 250, 350, nombre); // Asigna la nueva víbora como controlada por el usuario
        vivoras.add(nuevaVivora);
        viborasInterfaz.addElement(nuevaVivora);
        actualizarControl(nuevaVivora); // Actualiza el control de todas las víboras
        nuevaVivora.start();

        // Selecciona la nueva víbora en la lista para hacerla activa
        int index = viborasInterfaz.getSize() - 1;
        @SuppressWarnings("unchecked")
        JList<Vivora> snakeList = (JList<Vivora>) ((JScrollPane) ((JPanel) getContentPane().getComponent(1)).getComponent(1)).getViewport().getView();
        snakeList.setSelectedIndex(index);
    }

    public void actualizarJuego() {
        tablero.actualizarJuego();
    }

    private void actualizarControl(Vivora selectedVivora) {
        for (Vivora vivora : vivoras) {
            vivora.setControladaPorUsuario(vivora == selectedVivora);
        }
    }

    private void cambiarDificultad(int nuevaVelocidad) {
        Tablero.velocidadGlobal = nuevaVelocidad;
        timer.setDelay(Tablero.velocidadGlobal); // Actualiza el intervalo del temporizador
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Juego frame = new Juego();
            frame.setVisible(true);
        });
    }
}
