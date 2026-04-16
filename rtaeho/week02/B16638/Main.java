package B16638;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    static int N;
    static int max = Integer.MIN_VALUE;
    static boolean[] selected = new boolean[20];
    static String equation;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        equation = br.readLine();
        makeParentheses(0);
        System.out.print(max);
    }

    static void makeParentheses(int idx) {
        if (idx >= N) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < N; i++) {
                if (selected[i]) {
                    sb.append('(');
                    sb.append(equation.charAt(i));
                    sb.append(equation.charAt(i + 1));
                    sb.append(equation.charAt(i + 2));
                    sb.append(')');
                    i += 2;
                } else {
                    sb.append(equation.charAt(i));
                }
            }
            int result = calculate(sb.toString());
            max = Math.max(max, result);
            return;
        }

        if (idx + 2 < N) {
            selected[idx] = true;
            makeParentheses(idx + 4);
            selected[idx] = false;
        }
        makeParentheses(idx + 2);
    }

    static int calculate(String equation) {
        Deque<Character> operatorStack = new ArrayDeque<>();
        Deque<Integer> operandStack = new ArrayDeque<>();
        for (int i = 0; i < equation.length(); i++) {
            char e = equation.charAt(i);
            if (e - '0' >= 0 && e - '0' <= 9) {
                operandStack.push(e - '0');
            } else if (e == '(') {
                operatorStack.push(e);
            } else if (e == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    char operator = operatorStack.pop();
                    int operand2 = operandStack.pop();
                    int operand1 = operandStack.pop();
                    int result = applyOperator(operand1, operand2, operator);
                    operandStack.push(result);
                }
                operatorStack.pop();
            } else {
                while (!operatorStack.isEmpty() && getPriority(operatorStack.peek()) >= getPriority(e)) {
                    char operator = operatorStack.pop();
                    int operand2 = operandStack.pop();
                    int operand1 = operandStack.pop();
                    int result = applyOperator(operand1, operand2, operator);
                    operandStack.push(result);
                }
                operatorStack.push(equation.charAt(i));
            }
        }
        while (!operatorStack.isEmpty()) {
            char operator = operatorStack.pop();
            int operand2 = operandStack.pop();
            int operand1 = operandStack.pop();
            operandStack.push(applyOperator(operand1, operand2, operator));
        }
        return operandStack.pop();
    }

    static int getPriority(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*') {
            return 2;
        } else if (operator == '(') {
            return 0;
        }
        return 0;
    }

    static int applyOperator(int operand1, int operand2, char operator) {
        if (operator == '+') {
            return operand1 + operand2;
        } else if (operator == '-') {
            return operand1 - operand2;
        } else {
            return operand1 * operand2;
        }
    }
}
