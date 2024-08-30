package com.example.as0;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView no, historyTextView;
    ImageButton historyButton;
    Button one, two, three, four, five, six, seven, eight, nine, clear, equals, add, subtract, multiply, div, zero, decimal, mod, sign;
    StringBuilder currentInput = new StringBuilder();
    boolean isNewOp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        no = findViewById(R.id.no);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        zero = findViewById(R.id.zero);
        clear = findViewById(R.id.clear);
        equals = findViewById(R.id.equals);
        add = findViewById(R.id.add);
        subtract = findViewById(R.id.subtract);
        multiply = findViewById(R.id.multiply);
        div = findViewById(R.id.div);
        decimal = findViewById(R.id.decimal);
        mod = findViewById(R.id.mod);
        sign = findViewById(R.id.sign);
        historyButton = findViewById(R.id.HistoryButton);
        historyTextView = findViewById(R.id.historyTextView);

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (historyTextView.getVisibility() == View.GONE) {
                    historyTextView.setVisibility(View.VISIBLE);
                    historyTextView.setText(getHistory());
                } else {
                    historyTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void numberEvent(View view) {
        if (isNewOp) {
            currentInput.setLength(0);
            isNewOp = false;
        }

        Button clickedButton = (Button) view;
        currentInput.append(clickedButton.getText().toString());
        updateDisplay();
    }

    public void operationEvent(View view) {
        isNewOp = false;
        Button clickedButton = (Button) view;
        currentInput.append(" ").append(clickedButton.getText().toString()).append(" ");
        updateDisplay();
    }

    public void equalEvent(View view) {
        try {
            String expression = currentInput.toString();
            double result = evaluateExpression(expression);
            String resultText = String.valueOf(result);
            currentInput.setLength(0);
            currentInput.append(resultText);
            updateDisplay();
            appendToHistory(expression + " = " + resultText);
            isNewOp = true;
        } catch (Exception e) {
            no.setText("Error");
        }
    }

    public void clearEvent(View view) {
        currentInput.setLength(0);
        updateDisplay();
        isNewOp = true;
    }

    public void signChangeEvent(View view) {
        String text = no.getText().toString();
        if (text.startsWith("-")) {
            currentInput.delete(0, 1);
        } else {
            currentInput.insert(0, "-");
        }
        updateDisplay();
    }

    public void decimalEvent(View view) {
        String text = no.getText().toString();
        if (!text.contains(".")) {
            currentInput.append(".");
        }
        updateDisplay();
    }

    private void updateDisplay() {
        no.setText(currentInput.toString());
    }

    private void appendToHistory(String entry) {
        String existingHistory = historyTextView.getText().toString();
        if (!existingHistory.isEmpty()) {
            existingHistory += "\n";
        }
        historyTextView.setText(existingHistory + entry);
    }

    private String getHistory() {
        return historyTextView.getText().toString();
    }

    private double evaluateExpression(String expression) {
        List<Double> numbers = new ArrayList<>();
        List<Character> operators = new ArrayList<>();

        String[] tokens = expression.split(" ");
        for (String token : tokens) {
            if (isOperator(token)) {
                operators.add(token.charAt(0));
            } else {
                numbers.add(Double.parseDouble(token));
            }
        }

        // Perform * and / first
        for (int i = 0; i < operators.size(); i++) {
            char op = operators.get(i);
            if (op == '*' || op == '/') {
                double result = performOperation(numbers.get(i), numbers.get(i + 1), op);
                numbers.set(i, result);
                numbers.remove(i + 1);
                operators.remove(i);
                i--;
            }
        }

        // Perform + and - next
        for (int i = 0; i < operators.size(); i++) {
            char op = operators.get(i);
            double result = performOperation(numbers.get(i), numbers.get(i + 1), op);
            numbers.set(i, result);
            numbers.remove(i + 1);
            operators.remove(i);
            i--;
        }

        return numbers.get(0);
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("%");
    }

    private double performOperation(double num1, double num2, char operator) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            case '%':
                return num1 % num2;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
}
