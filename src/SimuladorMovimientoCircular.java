import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimuladorMovimientoCircular extends JFrame implements ActionListener {
    private JPanel panelIzquierdo, panelDerecho;
    private JButton startButton, stopButton, resetButton;
    private JTextField radioField, periodoField, masaField;
    private JLabel posicionXLabel, posicionYLabel, velocidadLabel, velocidadXLabel, velocidadYLabel, aceleracionLabel, aceleracionXLabel, aceleracionYLabel;
    private JLabel fuerzaCentripetaLabel, fuerzaXLabel, fuerzaYLabel;
    private Timer timer;
    private double radio, periodo, masa;
    private int centerX, centerY;
    private double angle = 0;
    private static final int PANEL_WIDTH = 600; // Ancho fijo del panel derecho
    private static final int PANEL_HEIGHT = 600; // Altura fija del panel derecho

    public SimuladorMovimientoCircular() {
        setTitle("Simulador de Movimiento Circular Uniforme");
        setSize(1000, 600);
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
        panelDerecho.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT)); // Establecer tamaño del panel derecho

        // Botones
        startButton = new JButton("Iniciar");
        startButton.addActionListener(this);
        stopButton = new JButton("Detener");
        stopButton.addActionListener(this);
        resetButton = new JButton("Resetear");
        resetButton.addActionListener(this);

        // Etiquetas para las coordenadas X & Y
        posicionXLabel = new JLabel("Posición en X: ");
        posicionYLabel = new JLabel("Posición en Y: ");
        velocidadLabel = new JLabel("Velocidad: ");
        velocidadXLabel = new JLabel("Velocidad en X: ");
        velocidadYLabel = new JLabel("Velocidad en Y: ");
        aceleracionLabel = new JLabel("Aceleración: ");
        aceleracionXLabel = new JLabel("Aceleración en X: ");
        aceleracionYLabel = new JLabel("Aceleración en Y: ");

        // Etiquetas para las fuerzas
        fuerzaCentripetaLabel = new JLabel("Fuerza Centrípeta: ");
        fuerzaXLabel = new JLabel("Fuerza en X: ");
        fuerzaYLabel = new JLabel("Fuerza en Y: ");

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
        panelIzquierdo.add(posicionXLabel);
        panelIzquierdo.add(posicionYLabel);
        panelIzquierdo.add(velocidadLabel);
        panelIzquierdo.add(velocidadXLabel);
        panelIzquierdo.add(velocidadYLabel);
        panelIzquierdo.add(aceleracionLabel);
        panelIzquierdo.add(aceleracionXLabel);
        panelIzquierdo.add(aceleracionYLabel);
        panelIzquierdo.add(fuerzaCentripetaLabel);
        panelIzquierdo.add(fuerzaXLabel);
        panelIzquierdo.add(fuerzaYLabel);

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
        int width = PANEL_WIDTH;
        int height = PANEL_HEIGHT;
        int divisiones = 30; // Número de divisiones en el plano cartesiano
        int maxValor = 300;
        int minValor = -300;

        g.setColor(Color.LIGHT_GRAY);

        // Dibujar líneas horizontales y etiquetas en el eje Y
        int deltaY = height / divisiones;
        for (int i = 1; i < divisiones; i++) {
            int y = i * deltaY;
            g.drawLine(0, y, width, y);
            int labelY = maxValor - i * 20;
            g.drawString(Integer.toString(labelY), 5, y + 5);
        }

        // Dibujar líneas verticales y etiquetas en el eje X
        int deltaX = width / divisiones;
        for (int i = 1; i < divisiones; i++) {
            int x = i * deltaX;
            g.drawLine(x, 0, x, height);
            int labelX = minValor + i * 20;
            g.drawString(Integer.toString(labelX), x - 10, height - 5);
        }

        // Dibujar ejes X y Y
        g.setColor(Color.BLACK);
        int midX = width / 2;
        int midY = height / 2;
        g.drawLine(midX, 0, midX, height); // Eje Y
        g.drawLine(0, midY, width, midY); // Eje X
    }

    private void dibujarCirculo(Graphics g) {
        // Calcular la posición del círculo
        double omega = 2 * Math.PI / periodo; // Velocidad angular
        double x = centerX + radio * Math.cos(angle);
        double y = centerY - radio * Math.sin(angle); // Usar resta para invertir el eje y

        // Limitar las coordenadas dentro del radio especificado
        x = Math.max(Math.min(x, centerX + radio), centerX - radio);
        y = Math.max(Math.min(y, centerY + radio), centerY - radio);

        // Dibujar el círculo en la nueva posición
        g.setColor(Color.RED);
        g.fillOval((int) (x - masa / 2), (int) (y - masa / 2), (int) masa, (int) masa);

        // Calcular y mostrar las coordenadas en el panel izquierdo
        // Dentro del método dibujarCirculo, después de calcular las aceleraciones
        // Calcular y mostrar las coordenadas en el panel izquierdo con 4 decimales
        double posX = x - centerX;
        double posY = centerY - y;
        posX = Math.round(posX * 10000.0) / 10000.0; // Redondear a 4 decimales
        posY = Math.round(posY * 10000.0) / 10000.0; // Redondear a 4 decimales
        posicionXLabel.setText("Posición en X: " + String.format("%.4f", posX));
        posicionYLabel.setText("Posición en Y: " + String.format("%.4f", posY));

        // Calcular velocidad tangencial con 4 decimales
        double velocidadTangencial = radio * (2 * Math.PI / periodo);
        velocidadTangencial = Math.round(velocidadTangencial * 10000.0) / 10000.0; // Redondear a 4 decimales
        velocidadLabel.setText("Velocidad: " + String.format("%.4f", velocidadTangencial));

        // Calcular y mostrar la velocidad y sus componentes en el panel izquierdo con 4 decimales
        double velocidadX = velocidadTangencial * Math.cos(angle);
        double velocidadY = velocidadTangencial * Math.sin(angle);
        velocidadX = Math.round(velocidadX * 10000.0) / 10000.0; // Redondear a 4 decimales
        velocidadY = Math.round(velocidadY * 10000.0) / 10000.0; // Redondear a 4 decimales
        velocidadXLabel.setText("Velocidad en X: " + String.format("%.4f", velocidadX));
        velocidadYLabel.setText("Velocidad en Y: " + String.format("%.4f", velocidadY));

        // Calcular aceleración tangencial con 4 decimales
        double aceleracionTangencial = -radio * (Math.pow(omega, 2));
        aceleracionTangencial = Math.round(aceleracionTangencial * 10000.0) / 10000.0; // Redondear a 4 decimales
        aceleracionLabel.setText("Aceleración: " + String.format("%.4f", aceleracionTangencial));

        // Calcular y mostrar la aceleración y sus componentes en el panel izquierdo con 4 decimales
        double aceleracionX = -aceleracionTangencial * Math.cos(angle);
        double aceleracionY = aceleracionTangencial * Math.sin(angle);
        aceleracionX = Math.round(aceleracionX * 10000.0) / 10000.0; // Redondear a 4 decimales
        aceleracionY = Math.round(aceleracionY * 10000.0) / 10000.0; // Redondear a 4 decimales
        aceleracionXLabel.setText("Aceleración en X: " + String.format("%.4f", aceleracionX));
        aceleracionYLabel.setText("Aceleración en Y: " + String.format("%.4f", aceleracionY));

        // Calcular la fuerza centrípeta con 4 decimales
        double fuerzaCentripeta = (masa * Math.pow(velocidadTangencial, 2)) / radio;
        fuerzaCentripeta = Math.round(fuerzaCentripeta * 10000.0) / 10000.0; // Redondear a 4 decimales
        fuerzaCentripetaLabel.setText("Fuerza Centrípeta: " + String.format("%.4f", fuerzaCentripeta));

        // Calcular y mostrar las componentes de la fuerza en X y Y con 4 decimales
        double fuerzaX = fuerzaCentripeta * Math.cos(angle);
        double fuerzaY = fuerzaCentripeta * Math.sin(angle);
        fuerzaX = Math.round(fuerzaX * 10000.0) / 10000.0; // Redondear a 4 decimales
        fuerzaY = Math.round(fuerzaY * 10000.0) / 10000.0; // Redondear a 4 decimales
        fuerzaXLabel.setText("Fuerza en X: " + String.format("%.4f", fuerzaX));
        fuerzaYLabel.setText("Fuerza en Y: " + String.format("%.4f", fuerzaY));
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

            centerX = PANEL_WIDTH / 2;
            centerY = PANEL_HEIGHT / 2;

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

            // Limitar las coordenadas dentro del radio especificado
            x = Math.max(Math.min(x, centerX + radio), centerX - radio);
            y = Math.max(Math.min(y, centerY + radio), centerY - radio);

            // Dibujar el círculo en la nueva posición
            Graphics g = panelDerecho.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
            dibujarCirculo(g);

            // Actualizar el ángulo para el siguiente frame
            panelDerecho.repaint();
            angle += omega * 0.05; // Incremento de ángulo basado en el tiempo (0.05 segundos)
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimuladorMovimientoCircular());
    }
}
