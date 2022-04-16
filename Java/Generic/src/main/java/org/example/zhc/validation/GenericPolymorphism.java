package org.example.zhc.validation;

class Parent<T>{
    T obj;
    public void setData(T obj){
        this.obj = obj;
    }
}

class Child extends Parent<String>{

    public void setData(String str){
        this.obj = str;
    }
}

public class GenericPolymorphism {

}
