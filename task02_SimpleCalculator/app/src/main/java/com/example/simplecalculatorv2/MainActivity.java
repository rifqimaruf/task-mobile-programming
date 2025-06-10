package com.example.simplecalculatorv2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView displayResult;
    private TextView displayExpression;
    private String currentInput = "";
    private double firstOperand = 0;
    private String operator = "";
    private boolean isOperatorClicked = false;
    private JSONArray historyArray = new JSONArray();
    private static final String HISTORY_FILE = "calculator_history.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayResult = findViewById(R.id.displayResult);
        displayExpression = findViewById(R.id.displayExpression); // Initialize the expression display
        loadHistory();

        setupButtons();
    }

    private void setupButtons() {
        // Number buttons setup
        int[] numberIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int id : numberIds) {
            Button button = findViewById(id);
            button.setOnClickListener(new NumberClickListener());
        }

        // Operator buttons setup
        int[] operatorIds = {
                R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide
        };

        for (int id : operatorIds) {
            Button button = findViewById(id);
            button.setOnClickListener(new OperatorClickListener());
        }

        // Function buttons
        findViewById(R.id.btnEquals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });

        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        findViewById(R.id.btnDecimal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.contains(".")) {
                    if (currentInput.isEmpty()) {
                        currentInput = "0.";
                    } else {
                        currentInput += ".";
                    }
                    updateDisplay();
                }
            }
        });

        findViewById(R.id.btnHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistory();
            }
        });
    }

    private class NumberClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if (isOperatorClicked) {
                currentInput = "";
                isOperatorClicked = false;
            }
            currentInput += button.getText().toString();
            updateDisplay();
        }
    }

    private class OperatorClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if (!currentInput.isEmpty()) {
                if (!operator.isEmpty()) {
                    calculate();
                }
                firstOperand = Double.parseDouble(currentInput);
                operator = button.getText().toString();
                isOperatorClicked = true;

                // Update expression display to show the operation
                updateExpressionDisplay();
            }
        }
    }

    private void updateDisplay() {
        displayResult.setText(currentInput);
    }

    // New method to update the expression display
    private void updateExpressionDisplay() {
        String formattedFirst = formatNumber(firstOperand);
        displayExpression.setText(formattedFirst + " " + operator);
    }

    // Helper method to format numbers (remove .0 from integers)
    private String formatNumber(double number) {
        return number % 1 == 0 ?
                String.valueOf((int) number) :
                String.valueOf(number);
    }

    private void calculate() {
        if (operator.isEmpty() || currentInput.isEmpty()) {
            return;
        }

        double secondOperand = Double.parseDouble(currentInput);
        double result = 0;
        String expression = formatNumber(firstOperand) + " " + operator + " " + formatNumber(secondOperand);

        // Update expression display to show complete expression
        displayExpression.setText(expression);

        switch (operator) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "-":
                result = firstOperand - secondOperand;
                break;
            case "×":
                result = firstOperand * secondOperand;
                break;
            case "÷":
                if (secondOperand == 0) {
                    displayResult.setText("Error");
                    Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                result = firstOperand / secondOperand;
                break;
        }

        // Format result to handle integer results nicely
        String resultStr = formatNumber(result);

        displayResult.setText(resultStr);

        // Save to history
        saveCalculation(expression, resultStr);

        // Reset for next calculation
        currentInput = resultStr;
        operator = "";
    }

    private void clear() {
        currentInput = "";
        operator = "";
        firstOperand = 0;
        isOperatorClicked = false;
        displayResult.setText("0");
        displayExpression.setText("");
    }

    private void saveCalculation(String expression, String result) {
        try {
            JSONObject calculation = new JSONObject();
            calculation.put("expression", expression);
            calculation.put("result", result);

            // Add timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            calculation.put("timestamp", sdf.format(new Date()));

            historyArray.put(calculation);
            saveHistory();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveHistory() {
        try {
            FileOutputStream fos = openFileOutput(HISTORY_FILE, MODE_PRIVATE);
            fos.write(historyArray.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save history", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadHistory() {
        File file = new File(getFilesDir(), HISTORY_FILE);
        if (!file.exists()) {
            historyArray = new JSONArray();
            return;
        }

        try {
            FileInputStream fis = openFileInput(HISTORY_FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            historyArray = new JSONArray(sb.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            historyArray = new JSONArray();
        }
    }

    private void showHistory() {
        if (historyArray.length() == 0) {
            Toast.makeText(this, "No calculation history", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONObject lastCalculation = historyArray.getJSONObject(historyArray.length() - 1);
            String message = "Last calculation: " +
                    lastCalculation.getString("expression") +
                    " = " +
                    lastCalculation.getString("result");
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}