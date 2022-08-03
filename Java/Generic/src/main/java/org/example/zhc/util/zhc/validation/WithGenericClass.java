package org.example.zhc.util.zhc.validation;

import java.util.Arrays;
import java.util.List;

public class WithGenericClass<T> {
    List<String> stringList;
    public void add(){
        Number a = 1;
        Class<? extends Number> nc = a.getClass();
        Number[] ns = Arrays.copyOf(new Integer[0], 0,Number[].class);
    }

    public <T> T[] toArray(T[] a) {
        return Arrays.copyOf(new Integer[0], 0, (Class<? extends T[]>) a.getClass());
    }
}
