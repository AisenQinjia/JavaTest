package org.example.zhc;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ClassAndTypeApp {
    @Test
    public void testType(){
        Map<String,Map<String,String>> mapType = new HashMap<>();

    }

    @Test
    public void typeErasure(){
        IntegerStack integers = new IntegerStack();
        Stack stack = integers;
        stack.push("String");
    }
}