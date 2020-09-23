package com.gateway.dashboard.hackerrank.easy;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MatchingBrackets {

    private final static Map<Character, Character> openClosePair = new HashMap<>();

    static {
        openClosePair.put(')', '(');
        openClosePair.put('}', '{');
        openClosePair.put(']', '[');
    }

    public static boolean isValidExpression(String expression) {
        Stack<Character> stack = new Stack<>();
        for(char ch : expression.toCharArray()) {
            if(openClosePair.containsKey(ch)) {
                if(stack.pop() != openClosePair.get(ch)) {
                    return false;
                }
            } else if(openClosePair.containsValue(ch)) {
                stack.push(ch);
            }
        }
        return stack.isEmpty();
    }

}
