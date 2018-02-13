package com.alyaromin.calculator;

import com.alyaromin.calculator.exception.CalcException;

import java.util.Scanner;

public class Controller {
    private View view;
    private Calculator calculator;

    private String expression;

    public Controller() {
        this.view = new View();
        this.calculator = new Calculator();
        view.update("");
    }

    public void run() {
        do {
            readExpressionFromConsole();
            try {
                String result = calculator.calculate(expression);
                view.update(expression + " = " + result);
            } catch (CalcException e) {
                view.update(expression + " = " + e.getMessage());
            }
        } while (!isQuit());
        view.update();
    }

    private boolean isQuit() {
        if (expression.compareToIgnoreCase("quit") == 0) {
            return true;
        }
        return false;
    }

    private void readExpressionFromConsole() {
        Scanner scanner = new Scanner(System.in);
        expression = scanner.nextLine();
    }
}
