package com.calc.onlinecalc;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Stack;

@Service
public class EquationSolverService {
    /**
     * Solves an equation by converting it from infix to reverse polish notation.
     * @param equation The equation to calculate
     * @return The result of the equation
     * @throws ArithmeticException If the equation could not be parsed correctly
     */
    public double solveEquation(String equation) throws ArithmeticException{
        ArrayList<String> rpn = parseToRPN(equation);
        Stack<String> stack = new Stack<>();
        for(String token : rpn) {
            if(token.matches("[0-9]+")) {
                stack.push(token);
            } else if (token.matches("[-+*/^]")) {
                if(stack.size() < 2) {
                    throw new ArithmeticException("Not enough numbers to apply operator to");
                }
                double right = Double.parseDouble(stack.pop());
                double left = Double.parseDouble(stack.pop());
                double r = switch (token) {
                    case "+" -> left + right;
                    case "-" -> left - right;
                    case "*" -> left * right;
                    case "/" -> left / right;
                    case "^" -> Math.pow(left, right);
                    default -> throw new ArithmeticException("Illegal Operand in equation");
                };
                stack.push(String.valueOf(r));
            }
        }
        return Double.parseDouble(stack.pop());
    }

    /**
     * Simple implementation of the shunting yard algorithm. Does not support arithmetic functions.
     * @param equation The equation string to parse
     * @return The reverse polish notation of the equation
     * @throws ArithmeticException If the equation cannot be parsed correctly
     */
    private static ArrayList<String> parseToRPN(String equation) throws ArithmeticException {
        Stack<String> stack = new Stack<>();
        ArrayList<String> rpn = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        for(int i = 0; i < equation.length(); i++) {
            char token = equation.charAt(i);
            // Check if token is valid
            if(!String.valueOf(token).matches("[0-9*/+^-]")) {
                throw new ArithmeticException("Equation contains illegal character");
            }
            // Always add numbers to the output
            if(Character.isDigit(token)) {
                // Parse the full integer number and not a single digit
                while(i < equation.length() && Character.isDigit(equation.charAt(i))) {
                    number.append(equation.charAt(i));
                    i++;
                }
                i--;
                rpn.add(number.toString());
                number.setLength(0);
            } else if ('(' == token) {
                // Always push opening brackets onto the stack
                stack.push(String.valueOf(token));
            } else if (')' == token) {
                // Add the stack to the output until stack empty or another bracket opens
                while(!stack.isEmpty() && !"(".equals(stack.peek())) {
                    rpn.add(stack.pop());
                }
            } else {
                // Add the stack to the output until stack empty or operator precedence is smaller or token has right associativity
                while(!stack.isEmpty()
                        && getOperatorPrecedence(token) <= getOperatorPrecedence(stack.peek().charAt(0))
                        && token != '^') {
                    rpn.add(stack.pop());
                }
                stack.push(String.valueOf(token));
            }
        }
        while(!stack.isEmpty()) {
            if("(".equals(stack.peek())) {
                throw new ArithmeticException("The equation contains unbalanced brackets");
            }
            rpn.add(stack.pop());
        }
        return rpn;
    }

    private static int getOperatorPrecedence(char c) {
        if(c == '+' || c == '-') {
            return 1;
        } else if (c == '*' || c == '/') {
            return 2;
        } else if (c == '^') {
            return 3;
        } else {
            return -1;
        }
    }
}
