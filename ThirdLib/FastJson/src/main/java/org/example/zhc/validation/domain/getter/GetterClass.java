package org.example.zhc.validation.domain.getter;

public class GetterClass extends AbstractGetterClass {
    int type;
    int level;
//    public GetterClass(){}
    public GetterClass(int type){
        this.type = type;
        level = 3;
    }
    public GetterClass(String type){
        this.type = Integer.parseInt(type);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level){
        this.level = level;
    }
}
