package org.example.zhc.validation;

import lombok.Getter;

public abstract class LogicTypeClass {
    @Getter
    private int type;
    LogicTypeClass(int type){
        this.type = type;
    }
    public abstract String apply();
    public static final LogicTypeClass MAIL = new LogicTypeClass(1) {
        @Override
        public String apply() {
            return "mail apply";
        }
    };
}
