package com.alyaromin.calculator;

public class View {
    public void update(String result) {
        clearConsole();
        System.out.println("Calculator by alyaromin");
        System.out.println("Type expression like \"(2 + 2) * 2\" to calculate");
        System.out.println("Type \"quit\" to quit");
        System.out.println("Result: " + result);
        System.out.print("Input: ");
    }

    public void update() {
        clearConsole();
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
