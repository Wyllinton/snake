package co.edu.uniquindio;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Juego extends JFrame {
    List<Vivora> vivoras;
    Tablero tablero;
    DefaultListModel<Vivora> vivorasDefault;
    Timer timer;

    public Juego() {
        setTitle("Snake");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        vivoras = new ArrayList<>();
        tablero = new Tablero(vivoras);

        // Panel lateral para controles y lista de víboras
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(250, getHeight()));

        // Botón para añadir una nueva víbora
        JButton agregarVivoraButton = new JButton("Añadir Vívora");
        agregarVivoraButton.addActionListener(e -> agregarVivora());
        sidePanel.add(agregarVivoraButton, BorderLayout.SOUTH);

        // Lista de víboras
        vivorasDefault = new DefaultListModel<>();
        JList<Vivora> snakeList = new JList<>(vivorasDefault);
        snakeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        snakeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Vivora selectedVivora = snakeList.getSelectedValue();
                if (selectedVivora != null) {
                    actualizarControl(selectedVivora);
                    tablero.requestFocusInWindow(); // Solicitar el enfoque para el panel del juego
                }
            }
        });
        JScrollPane snakeListScrollPane = new JScrollPane(snakeList);
        sidePanel.add(snakeListScrollPane, BorderLayout.CENTER);

        // Panel de dificultad
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new FlowLayout());
        
        JButton hardButton = new JButton("Fácil");
        hardButton.addActionListener(e -> cambiarDificultad(100)); // Velocidad rápida para difícil

        JButton normalButton = new JButton("Difícil");
        normalButton.addActionListener(e -> cambiarDificultad(50)); // Velocidad lenta para fácil

        difficultyPanel.add(normalButton);
        difficultyPanel.add(hardButton);
        
        sidePanel.add(difficultyPanel, BorderLayout.NORTH); // Añade el panel de dificultad en la parte inferior

        // Añade los paneles al marco principal
        add(tablero, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.WEST);

        // Crea la primera víbora controlada por el usuario
        agregarVivoraInicial();

        // Inicia el bucle del juego
        timer = new Timer(Tablero.velocidadGlobal, e -> actualizarJuego());
        timer.start();
    }

    private void agregarVivoraInicial() {
        String nombre = "Vívora 1";
        Vivora primeraVivora = new Vivora(true, 200, 200, nombre);
        vivoras.add(primeraVivora);
        vivorasDefault.addElement(primeraVivora);
        actualizarControl(primeraVivora);
        primeraVivora.start();
    }

    private void agregarVivora() {
        String nombre = "Vívora " + (vivoras.size() + 1);
        Vivora nuevaVivora = new Vivora(true, 200, 200, nombre); // Asigna la nueva víbora como controlada por el usuario
        vivoras.add(nuevaVivora);
        vivorasDefault.addElement(nuevaVivora);
        actualizarControl(nuevaVivora); // Actualiza el control de todas las víboras
        nuevaVivora.start();

        // Selecciona la nueva víbora en la lista para hacerla activa
        int index = vivorasDefault.getSize() - 1;
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
