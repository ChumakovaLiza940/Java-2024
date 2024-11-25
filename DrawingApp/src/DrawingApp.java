import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

interface Tool {
    void draw(Graphics g, int x, int y);
}

class Pencil implements Tool {
    @Override
    public void draw(Graphics g, int x, int y) {
        g.fillRect(x, y, 2, 2);
    }
}

class Brush implements Tool {
    @Override
    public void draw(Graphics g, int x, int y) {
        g.fillOval(x, y, 10, 10);
    }
}

class Eraser implements Tool {
    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.fillRect(x - 5, y - 5, 20, 20);
    }
}

class ToolFactory {
    public static Tool createTool(String toolType) {
        switch (toolType) {
            case "Карандаш":
                return new Pencil();
            case "Кисть":
                return new Brush();
            case "Ластик":
                return new Eraser();
            default:
                throw new IllegalArgumentException("Неизвестный инструмент: " + toolType);
        }
    }
}

public class DrawingApp {
    private static Tool currentTool = ToolFactory.createTool("Карандаш");
    private static Color currentColor = Color.BLACK;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Приложение для рисования");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.WHITE);
            }
        };

        drawingPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Graphics g = drawingPanel.getGraphics();
                g.setColor(currentColor);
                currentTool.draw(g, e.getX(), e.getY());
                g.dispose();
            }
        });

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout());

        JButton pencilButton = new JButton("Карандаш");
        JButton brushButton = new JButton("Кисть");
        JButton eraserButton = new JButton("Ластик");

        configureButtonStyle(pencilButton);
        configureButtonStyle(brushButton);
        configureButtonStyle(eraserButton);

        pencilButton.addActionListener(e -> currentTool = ToolFactory.createTool("Карандаш"));
        brushButton.addActionListener(e -> currentTool = ToolFactory.createTool("Кисть"));
        eraserButton.addActionListener(e -> currentTool = ToolFactory.createTool("Ластик"));

        JComboBox<String> colorPicker = new JComboBox<>(new String[]{
                "Черный", "Синий", "Красный", "Зеленый", "Желтый", "Розовый", "Оранжевый", "Фиолетовый"
        });

        configureComboBoxStyle(colorPicker);

        Color[] colors = {
                Color.BLACK, Color.BLUE, Color.RED, Color.GREEN,
                Color.YELLOW, Color.PINK, Color.ORANGE, new Color(128, 0, 128)
        };

        colorPicker.addActionListener(e -> {
            int selectedIndex = colorPicker.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < colors.length) {
                currentColor = colors[selectedIndex];
            }
        });

        toolbar.add(pencilButton);
        toolbar.add(brushButton);
        toolbar.add(eraserButton);
        toolbar.add(new JLabel("Цвет:"));
        toolbar.add(colorPicker);

        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(drawingPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static void configureButtonStyle(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
    }

    private static void configureComboBoxStyle(JComboBox<String> comboBox) {
        comboBox.setBackground(Color.BLACK);
        comboBox.setForeground(Color.WHITE);
        comboBox.setOpaque(true);
        comboBox.setFocusable(false);

        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBackground(Color.BLACK);
                button.setForeground(Color.WHITE);
                return button;
            }
        });
    }
}
