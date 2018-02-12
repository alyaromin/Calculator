package com.alyaromin.calculator;

import com.alyaromin.calculator.exception.CalcException;

import java.util.Scanner;

public class Controller {
    private View view;
    private Calculator calculator;
    private Scanner scanner;
    private String expression;
    private String expressionRPN;
    private String result;
    private boolean isNotQuit;

    public Controller() {
        this.scanner = new Scanner(System.in);
        this.view = new View();
        this.calculator = new Calculator();
        reset();
        view.update(result);
    }

    public void run() {
        while (isNotQuit) {
            reset();
            getExpression();
            try {
                result = calculator.calculate(expression);
                view.update(expression + " = " + result);
            } catch (CalcException e) {
                view.update(expression + " = " + e.getMessage());
            }
        }
        view.update();
    }

    private void getExpression() {
        expression = scanner.nextLine();
        if (expression.compareToIgnoreCase("quit") == 0) {
            isNotQuit = false;
        }
    }

    private void reset() {
        this.expression = "";
        this.expressionRPN = "";
        this.result = "";
        this.isNotQuit = true;
    }
}
