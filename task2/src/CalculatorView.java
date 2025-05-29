import javax.swing.*;
import java.awt.*;

public class CalculatorView extends JFrame {
    private JTextField inputField;
    private JButton calculateButton;
    private JLabel resultLabel;

    public CalculatorView() {
        setTitle("Advanced Calculator");
        setSize(500, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        inputField = new JTextField(30);
        calculateButton = new JButton("Calculate");
        resultLabel = new JLabel("Result: ");

        add(inputField);
        add(calculateButton);
        add(resultLabel);

        // Добавляем подсказку о поддерживаемых операциях
        JLabel hint = new JLabel("Supported operations: + - * / ^ | (factorial) log() exp()");
        add(hint);
    }

    public String getInput() {
        return inputField.getText();
    }

    public void setResult(String result) {
        resultLabel.setText("Result: " + result);
    }

    public void addCalculateListener(java.awt.event.ActionListener listenForCalculateButton) {
        calculateButton.addActionListener(listenForCalculateButton);
    }
}