package com.alyaromin.calculator;

import com.alyaromin.calculator.exception.CalcException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Stack;
import java.util.StringTokenizer;

public class Calculator {
    private final String OPERATORS = "+-*/^";
    private Stack<String> operations = new Stack<String>();
    private Stack<String> expressionRPN = new Stack<String>();
    private Stack<String> result = new Stack<String>();
    private String expression;

    public String calculate(String expression) throws CalcException {
        this.expression = expression;
        parseExpressionToRPN();

        while (!expressionRPN.empty()) {
            String token = expressionRPN.pop();
            if (isNumber(token)) {
                result.push(token);
            } else {
                switch (token.charAt(0)) {
                    case '+': {
                        BigDecimal a = getNumber();
                        BigDecimal b = getNumber();
                        a = b.add(a);
                        result.push(a.toString());
                        break;
                    }
                    case '-': {
                        BigDecimal a = getNumber();
                        BigDecimal b = getNumber();
                        a = b.subtract(a);
                        result.push(a.toString());
                        break;
                    }
                    case '*': {
                        BigDecimal a = getNumber();
                        BigDecimal b = getNumber();
                        a = b.multiply(a);
                        result.push(a.toString());
                        break;
                    }
                    case '/': {
                        BigDecimal a = getNumber();
                        BigDecimal b = getNumber();
                        if (a.compareTo(new BigDecimal("0")) == 0) {
                            throw new CalcException("Деление на 0");
                        }
                        a = b.divide(a, 8, BigDecimal.ROUND_HALF_UP);
                        result.push(a.toString());
                        break;
                    }
                    case '^': {
                        BigDecimal a = getNumber();
                        BigDecimal b = getNumber();
                        a = BigDecimal.valueOf(Math.pow(b.doubleValue(), a.doubleValue()));
                        result.push(a.toString());
                        break;
                    }
                }
            }

        }
        return result.pop();
    }

    public void parseExpressionToRPN() throws CalcException {
        boolean isPrevTokenNumber = false;
        boolean isNextNumberNegate = false;
        operations.clear();
        expressionRPN.clear();

        StringTokenizer stringTokenizer = new StringTokenizer(expression,
                OPERATORS + "()" + " ", true);

        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();

            if (isInvalidToken(token)) {
                throw new CalcException("Невалидные данные");
            }

            if (isNumber(token)) {
                if (isPrevTokenNumber) {
                    throw new CalcException("Невалидные данные");
                }
                if (isNextNumberNegate) {
                    token = "-" + token;
                    isNextNumberNegate = false;
                }
                expressionRPN.push(token.replace(",", "."));
                isPrevTokenNumber = true;
            }

            if (isOperator(token)) {
                if (token.equals("-")) {
                    if (!isPrevTokenNumber && operations.empty() || !operations.empty() && !operations.lastElement().equals(")")) {
                        isNextNumberNegate = true;
                    } else {
                        while (!operations.empty() && isOperator(operations.lastElement())
                                && getPriority(token) <= getPriority(operations.lastElement())) {
                            expressionRPN.push(operations.pop());
                        }
                        operations.push(token);
                        isPrevTokenNumber = false;
                    }
                } else {
                    while (!operations.empty() && isOperator(operations.lastElement())
                            && getPriority(token) <= getPriority(operations.lastElement())) {
                        expressionRPN.push(operations.pop());
                    }
                    operations.push(token);
                    isPrevTokenNumber = false;
                }
            }

            if (isOpenBracket(token)) {
                operations.push(token);
                isPrevTokenNumber = false;
            }

            if (isCloseBracket(token)) {
                if (operations.empty()) {
                    throw new CalcException("Не хватает открытой скобки");
                }
                while (!isOpenBracket(operations.lastElement())) {
                    expressionRPN.push(operations.pop());
                }
                operations.pop();
                isPrevTokenNumber = false;
            }
        }

        while (!operations.empty()) {
            if (isOpenBracket(operations.lastElement())) {
                throw new CalcException("Не хватает закрытой скобки");
            }
            expressionRPN.push(operations.pop());
        }
        Collections.reverse(expressionRPN);
    }

    private boolean isInvalidToken(String token) {
        if (!isNumber(token)
                && !isOpenBracket(token)
                && !isCloseBracket(token)
                && !isOperator(token)
                && !token.equals(" ")
                && !token.equals(".")
                && !token.equals(",")) {
            return true;
        }
        return false;
    }

    private boolean isNumber(String token) {
        try {
            token = token.replace(",", ".");
            new BigDecimal(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    private boolean isCloseBracket(String token) {
        return token.equals(")");
    }

    private boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    private byte getPriority(String token) {
        if (token.equals("+") || token.equals("-")) {
            return 1;
        }
        if (token.equals("*") || token.equals("/")) {
            return 2;
        }
        return 3;
    }

    private BigDecimal getNumber() {
        BigDecimal number = new BigDecimal(result.pop());
        return number;
    }
}
