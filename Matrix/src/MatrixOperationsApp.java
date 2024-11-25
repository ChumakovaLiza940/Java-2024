import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

interface MatrixOperation {
    String execute(int[][] matrix1, int[][] matrix2);
}

class MatrixAddition implements MatrixOperation {
    @Override
    public String execute(int[][] matrix1, int[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix1[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }

        return MatrixOperationsApp.matrixToString(result);
    }
}

class MatrixMultiplication implements MatrixOperation {
    @Override
    public String execute(int[][] matrix1, int[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix2[0].length;
        int common = matrix1[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = 0;
                for (int k = 0; k < common; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return MatrixOperationsApp.matrixToString(result);
    }
}

class MatrixDeterminant implements MatrixOperation {
    @Override
    public String execute(int[][] matrix1, int[][] matrix2) {
        int determinant = determinant(matrix1);
        return "Определитель: " + determinant;
    }

    private int determinant(int[][] matrix) {
        int n = matrix.length;
        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        int det = 0;
        for (int i = 0; i < n; i++) {
            int[][] subMatrix = new int[n-1][n-1];
            for (int j = 1; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (k < i) subMatrix[j-1][k] = matrix[j][k];
                    else if (k > i) subMatrix[j-1][k-1] = matrix[j][k];
                }
            }
            det += (i % 2 == 0 ? 1 : -1) * matrix[0][i] * determinant(subMatrix);
        }
        return det;
    }
}

interface MatrixOperationFactory {
    MatrixOperation createOperation();
}

class MatrixAdditionFactory implements MatrixOperationFactory {
    @Override
    public MatrixOperation createOperation() {
        return new MatrixAddition();
    }
}

class MatrixMultiplicationFactory implements MatrixOperationFactory {
    @Override
    public MatrixOperation createOperation() {
        return new MatrixMultiplication();
    }
}

class MatrixDeterminantFactory implements MatrixOperationFactory {
    @Override
    public MatrixOperation createOperation() {
        return new MatrixDeterminant();
    }
}

public class MatrixOperationsApp {
    private static JTextArea resultArea;
    private static JTextField matrix1Field, matrix2Field;
    private static JTextField rowsField, colsField;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Операции с матрицами");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Сложение");
        JButton multiplyButton = new JButton("Умножение");
        JButton determinantButton = new JButton("Определитель");

        configureButtonStyle(addButton);
        configureButtonStyle(multiplyButton);
        configureButtonStyle(determinantButton);

        panel.add(new JLabel("Размерность матриц:"));
        rowsField = new JTextField(5);
        colsField = new JTextField(5);
        panel.add(new JLabel("Строки:"));
        panel.add(rowsField);
        panel.add(new JLabel("Столбцы:"));
        panel.add(colsField);

        panel.add(new JLabel("Матрица 1 (через пробелы):"));
        matrix1Field = new JTextField(30);
        panel.add(matrix1Field);

        panel.add(new JLabel("Матрица 2 (через пробелы):"));
        matrix2Field = new JTextField(30);
        panel.add(matrix2Field);

        resultArea = new JTextArea(5, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation(new MatrixAdditionFactory());
            }
        });

        multiplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation(new MatrixMultiplicationFactory());
            }
        });

        determinantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performOperation(new MatrixDeterminantFactory());
            }
        });

        panel.add(addButton);
        panel.add(multiplyButton);
        panel.add(determinantButton);
        panel.add(scrollPane);

        frame.add(panel);

        frame.setVisible(true);
    }

    private static void configureButtonStyle(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
    }

    private static void performOperation(MatrixOperationFactory factory) {
        try {
            int rows = Integer.parseInt(rowsField.getText());
            int cols = Integer.parseInt(colsField.getText());

            int[][] matrix1 = parseMatrix(matrix1Field.getText(), rows, cols);
            int[][] matrix2 = parseMatrix(matrix2Field.getText(), rows, cols);

            MatrixOperation operation = factory.createOperation();
            String result = operation.execute(matrix1, matrix2);
            resultArea.setText(result);
        } catch (Exception ex) {
            resultArea.setText("Ошибка ввода! Убедитесь, что размерности и элементы введены правильно.");
        }
    }

    private static int[][] parseMatrix(String matrixText, int rows, int cols) {
        String[] elements = matrixText.split(" ");
        if (elements.length != rows * cols) {
            throw new IllegalArgumentException("Количество элементов в матрице не совпадает с заданными размерами.");
        }

        int[][] matrix = new int[rows][cols];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Integer.parseInt(elements[index++]);
            }
        }
        return matrix;
    }
    
    public static String matrixToString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            for (int el : row) {
                sb.append(el).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
