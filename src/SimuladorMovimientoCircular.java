import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SimuladorMovimientoCircular extends JFrame implements ActionListener {
    private JPanel panelIzquierdo, panelDerecho;
    private JButton startButton, stopButton, resetButton;
    private JTextField radioField, periodoField, masaField;
    private Timer timer;
    private double radio, periodo, masa;
    private int centerX, centerY;
    private double angle = 0;

    public SimuladorMovimientoCircular() {
        setTitle("Simulador de Movimiento Circular Uniforme");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Paneles
        panelIzquierdo = new JPanel();
        panelIzquierdo.setPreferredSize(new Dimension(200, 600));
        panelDerecho = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarPlanoCartesiano(g);
                dibujarCirculo(g);
            }
        };

        // Botones
        startButton = new JButton("Iniciar");
        startButton.addActionListener(this);
        stopButton = new JButton("Detener");
        stopButton.addActionListener(this);
        resetButton = new JButton("Resetear");
        resetButton.addActionListener(this);

        // Campos de texto
        radioField = new JTextField(10);
        periodoField = new JTextField(10);
        masaField = new JTextField(10);

        // Añadir componentes al panel izquierdo
        panelIzquierdo.add(new JLabel("Radio (m): "));
        panelIzquierdo.add(radioField);
        panelIzquierdo.add(new JLabel("Periodo (s): "));
        panelIzquierdo.add(periodoField);
        panelIzquierdo.add(new JLabel("Masa (kg): "));
        panelIzquierdo.add(masaField);
        panelIzquierdo.add(startButton);
        panelIzquierdo.add(stopButton);
        panelIzquierdo.add(resetButton);

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);

        // Inicializar valores
        radio = 100; // Valor por defecto
        periodo = 2; // Valor por defecto
        masa = 1; // Valor por defecto

        // Inicializar temporizador
        timer = new Timer(50, this); // 50ms para actualizar la animación

        setVisible(true);
    }

    private void dibujarPlanoCartesiano(Graphics g) {
        int width = panelDerecho.getWidth();
        int height = panelDerecho.getHeight();
        int divisiones = 10; // Número de divisiones en el plano cartesiano

        g.setColor(Color.LIGHT_GRAY);

        // Dibujar líneas horizontales
        int deltaY = height / divisiones;
        for (int i = 1; i < divisiones; i++) {
            int y = i * deltaY;
            g.drawLine(0, y, width, y);
        }

        // Dibujar líneas verticales
        int deltaX = width / divisiones;
        for (int i = 1; i < divisiones; i++) {
            int x = i * deltaX;
            g.drawLine(x, 0, x, height);
        }

        // Dibujar ejes X y Y
        g.setColor(Color.BLACK);
        g.drawLine(0, height / 2, width, height / 2); // Eje X
        g.drawLine(width / 2, 0, width / 2, height); // Eje Y
    }

    private void dibujarCirculo(Graphics g) {
        // Calcular la posición del círculo
        double omega = 2 * Math.PI / periodo; // Velocidad angular
        double x = centerX + radio * Math.cos(angle);
        double y = centerY - radio * Math.sin(angle); // Usar resta para invertir el eje y

        // Dibujar el círculo en la nueva posición
        g.setColor(Color.RED);
        g.fillOval((int) (x - masa / 2), (int) (y - masa / 2), (int) masa, (int) masa);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            try {
                radio = Double.parseDouble(radioField.getText());
                periodo = Double.parseDouble(periodoField.getText());
                masa = Double.parseDouble(masaField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese valores válidos para radio, periodo y masa.");
                return;
            }

            centerX = panelDerecho.getWidth() / 2;
            centerY = panelDerecho.getHeight() / 2;

            timer.start();
        } else if (e.getSource() == stopButton) {
            timer.stop();
        } else if (e.getSource() == resetButton) {
            radioField.setText("");
            periodoField.setText("");
            masaField.setText("");
            timer.stop();
        }

        if (e.getSource() == timer) {
            // Calcular la posición del círculo
            double omega = 2 * Math.PI / periodo; // Velocidad angular
            double x = centerX + radio * Math.cos(angle);
            double y = centerY - radio * Math.sin(angle); // Restamos el término para invertir el eje y

            // Dibujar el círculo en la nueva posición
            Graphics g = panelDerecho.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, panelDerecho.getWidth(), panelDerecho.getHeight());
            g.setColor(Color.RED);
            g.fillOval((int) (x - masa / 2), (int) (y - masa / 2), (int) masa, (int) masa);

            // Actualizar el ángulo para el siguiente frame
            panelDerecho.repaint();
            angle += omega * 0.05; // Incremento de ángulo basado en el tiempo (0.05 segundos)
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimuladorMovimientoCircular());
    }
}





