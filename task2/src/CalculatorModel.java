import java.util.Stack;

public class CalculatorModel {
    public double evaluate(String expression) throws Exception {
        // Проверка баланса скобок
        if (!checkParenthesesBalance(expression)) {
            throw new Exception("Unbalanced parentheses");
        }

        expression = expression.replaceAll("\\s+", "");
        return evaluateExpression(expression);
    }

    private boolean checkParenthesesBalance(String expression) {
        int balance = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }

    private double evaluateExpression(String expression) throws Exception {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);

            if (Character.isDigit(current) || current == '-' && (i == 0 || !Character.isDigit(expression.charAt(i-1)))) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                numbers.push(Double.parseDouble(sb.toString()));
                i--;
            } else if (current == '(') {
                operations.push(current);
            } else if (current == ')') {
                while (!operations.isEmpty() && operations.peek() != '(') {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.pop();
            } else if (isOperation(current)) {
                while (!operations.isEmpty() && precedence(operations.peek()) >= precedence(current)) {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.push(current);
            } else if (current == '|') {
                // Обработка факториала
                if (i == 0 || !Character.isDigit(expression.charAt(i-1))) {
                    throw new Exception("Invalid factorial position");
                }
                double num = numbers.pop();
                numbers.push(factorial(num));
            } else if (current == 'e' && i+2 < expression.length() && expression.substring(i, i+3).equals("exp")) {
                // Обработка exp()
                i += 2; // Пропускаем "exp"
                if (expression.charAt(i+1) != '(') throw new Exception("Invalid exp syntax");
                int end = findClosingParenthesis(expression, i+1);
                double arg = evaluateExpression(expression.substring(i+2, end));
                numbers.push(Math.exp(arg));
                i = end;
            } else if (current == 'l' && i+3 < expression.length() && expression.substring(i, i+3).equals("log")) {
                // Обработка log()
                i += 2; // Пропускаем "log"
                if (expression.charAt(i+1) != '(') throw new Exception("Invalid log syntax");
                int end = findClosingParenthesis(expression, i+1);
                double arg = evaluateExpression(expression.substring(i+2, end));
                numbers.push(Math.log(arg)/Math.log(2)); // Логарифм по основанию 2
                i = end;
            }
        }

        while (!operations.isEmpty()) {
            numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
        }

        if (numbers.size() != 1) {
            throw new Exception("Invalid expression");
        }

        return numbers.pop();
    }

    private int findClosingParenthesis(String expression, int start) throws Exception {
        int balance = 1;
        for (int i = start + 1; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') balance++;
            if (expression.charAt(i) == ')') balance--;
            if (balance == 0) return i;
        }
        throw new Exception("No closing parenthesis");
    }

    private boolean isOperation(char c) {
        return "+-*/^".indexOf(c) >= 0;
    }

    private double factorial(double n) throws Exception {
        if (n < 0 || n != (int)n) {
            throw new Exception("Factorial is defined only for non-negative integers");
        }
        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
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

    private double applyOperation(char operation, double b, double a) throws Exception {
        switch (operation) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            case '^':
                return Math.pow(a, b);
            default:
                throw new Exception("Unknown operation: " + operation);
        }
    }
}
