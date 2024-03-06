import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CircularMotionSimulation extends JPanel implements ActionListener {
    private int centerX;
    private int centerY;
    private int squareSize;
    private int squareX;
    private int squareY;
    private int radius;
    private double angle = 0;
    private Timer timer;
    private final int divisions = 20; // Número de divisiones en cada eje
    private JButton startButton;
    private JSlider speedSlider; // Nuevo control deslizante para ajustar la velocidad
    private boolean isRunning = false;
    private final double KILOMETERS_TO_PIXELS = 10; // Escala: 1 km/h equivale a 10 píxeles por segundo
    private final double RADIUS_KILOMETERS = 10; // Radio del círculo en kilómetros

    public CircularMotionSimulation() {
        timer = new Timer(20, this);

        // Crear botón "Iniciar"
        startButton = new JButton("Iniciar");
        startButton.setPreferredSize(new Dimension(100, 40)); // Establecer el tamaño preferido del botón
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isRunning = !isRunning;
                if (isRunning) {
                    startButton.setText("Detener");
                    timer.start();
                } else {
                    startButton.setText("Iniciar");
                    timer.stop();
                }
            }
        });

        // Crear control deslizante para ajustar la velocidad
        speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        speedSlider.setPreferredSize(new Dimension(100, 40)); // Establecer el tamaño preferido del control deslizante
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int value = speedSlider.getValue();
                timer.setDelay(value); // Establecer el valor del control deslizante como la velocidad del temporizador
            }
        });

        // Agregar componentes al panel
        setLayout(new BorderLayout());

        // Crear panel para la simulación del movimiento
        JPanel simulationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Calcular el tamaño del cuadrado y sus coordenadas
                squareSize = Math.min(getWidth(), getHeight()) - 20;
                squareX = 10;
                squareY = (getHeight() - squareSize) / 2;

                // Dibujar el cuadrado
                g2d.setColor(Color.BLUE);
                g2d.drawRect(squareX, squareY, squareSize, squareSize);

                // Establecer el centro del cuadrado como (0, 0)
                centerX = squareX + squareSize / 2;
                centerY = squareY + squareSize / 2;

                // Dibujar plano cartesiano
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawLine(0, centerY, getWidth(), centerY); // Eje X
                g2d.drawLine(centerX, 0, centerX, getHeight()); // Eje Y

                // Calcular el radio del círculo en píxeles
                radius = (int) (RADIUS_KILOMETERS * KILOMETERS_TO_PIXELS);

                // Calcular el espaciado entre las divisiones
                int divisionSpacingX = squareSize / (divisions * 2);
                int divisionSpacingY = squareSize / (divisions * 2);

                // Dibujar divisiones y valores en el eje X
                for (int i = -divisions; i <= divisions; i += 2) {
                    int x = centerX + i * divisionSpacingX;
                    g2d.drawLine(x, centerY - 5, x, centerY + 5);
                    g2d.drawString(String.valueOf(i), x - 5, centerY + 20);
                }

                // Dibujar divisiones y valores en el eje Y
                for (int i = -divisions; i <= divisions; i += 2) {
                    int y = centerY - i * divisionSpacingY;
                    g2d.drawLine(centerX - 5, y, centerX + 5, y);
                    g2d.drawString(String.valueOf(i), centerX + 10, y + 5);
                }

                // Dibujar círculo en movimiento si está en ejecución
                if (isRunning) {
                    double angularSpeed = (speedSlider.getValue() * KILOMETERS_TO_PIXELS) / radius; // Velocidad angular en radianes por segundo
                    angle += angularSpeed * 0.05; // Incremento angular basado en la velocidad angular
                    int x = (int) (centerX + radius * Math.cos(angle));
                    int y = (int) (centerY - radius * Math.sin(angle)); // Resta en y porque la coordenada Y aumenta hacia abajo
                    g2d.setColor(Color.RED);
                    g2d.fillOval(x - 10, y - 10, 20, 20);
                }
            }
        };

        // Crear panel para los controles
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.add(Box.createVerticalGlue());
        controlsPanel.add(startButton);
        controlsPanel.add(new JLabel("Velocidad (km/h):"));
        controlsPanel.add(speedSlider);
        controlsPanel.add(Box.createVerticalGlue());

        // Crear JSplitPane para dividir la ventana
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, simulationPanel, controlsPanel);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        // Establecer el tamaño preferido del panel
        setPreferredSize(new Dimension(800, 400));

        // Establecer el tamaño del panel de simulación
        simulationPanel.setPreferredSize(new Dimension(600, 400));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Circular Motion Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new CircularMotionSimulation(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        frame.setVisible(true);
    }
}
