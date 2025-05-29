
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class CalculatorController {
    private CalculatorModel model;
    private CalculatorView view;

    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;

        this.view.addCalculateListener(new CalculateListener());
    }

    class CalculateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = view.getInput();
            try {
                double result = model.evaluate(input);
                view.setResult(String.valueOf(result));
            } catch (Exception ex) {
                view.setResult("Error: " + ex.getMessage());
            }
        }
    }
}

