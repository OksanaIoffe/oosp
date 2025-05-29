import javax.swing.*;
import java.awt.*;

public class CalculatorView extends JFrame {
    private JTextField inputField;
    private JButton calculateButton;
    private JLabel resultLabel;

    public CalculatorView() {
        setTitle("Calculator");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        inputField = new JTextField(20);
        calculateButton = new JButton("Calculate");
        resultLabel = new JLabel("Result: ");

        add(inputField);
        add(calculateButton);
        add(resultLabel);
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