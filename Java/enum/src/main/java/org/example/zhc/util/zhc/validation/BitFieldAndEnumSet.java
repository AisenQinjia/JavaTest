package org.example.zhc.util.zhc.validation;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

public class BitFieldAndEnumSet {
    public static class BitFieldEnum{
        public static final int FIRST_BIT = 1;
        public static final int SECOND_BIT = 1<<1;
        public static final int THIRD_BIT = 1<<2;
    }
    public enum Style {
        BOLD,
        UNDERLINE
    }
    @Test
    public void enumSet(){
        EnumSet<Style> StyleSet = EnumSet.allOf(Style.class);
        boolean contains = StyleSet.contains(Style.BOLD);
        System.out.println("StyleSet.contains(Style.BOLD): " + contains);
    }
}
