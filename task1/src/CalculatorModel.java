import java.util.Stack;

public class CalculatorModel {
    public double evaluate(String expression) {
        // Удаляем пробелы
        expression = expression.replaceAll("\s+", "");
        return evaluateExpression(expression);
    }

    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);

            if (Character.isDigit(current) || current == '-') {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                numbers.push(Double.parseDouble(sb.toString()));
                i--; // Уменьшаем индекс, чтобы не пропустить символ
            } else if (current == '(') {
                operations.push(current);
            } else if (current == ')') {
                while (!operations.isEmpty() && operations.peek() != '(') {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.pop(); // Убираем '('
            } else if ("+-*/^".indexOf(current) >= 0) {
                while (!operations.isEmpty() && precedence(operations.peek()) >= precedence(current)) {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.push(current);
            }
        }

        while (!operations.isEmpty()) {
            numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private int precedence(char operation) {
        switch (operation) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }

    private double applyOperation(char operation, double b, double a) {
        switch (operation) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b; // Деление с плавающей точкой
            case '^':
                return Math.pow(a, b);
            default:
                return 0;
        }
    }
}