package org.example.zhc.util.zhc.validation;

public enum LogicType {
    MAIL(1){

        @Override
        public String apply() {
            return "null";
        }
    },
    ANNOUNCEMENT(2){
        @Override
        public String apply() {
            return null;
        }
    };

    private int type;
    LogicType(int type){
        this.type = type;
    }
    public int type(){
        return type;
    }
    public abstract String apply();
}
