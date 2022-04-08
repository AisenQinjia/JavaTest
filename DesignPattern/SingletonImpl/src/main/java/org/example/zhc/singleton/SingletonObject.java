package org.example.zhc.singleton;

public class SingletonObject {
    private static volatile SingletonObject singletonObject;
    private SingletonObject(){}
    public static SingletonObject getInstance(){
        if(singletonObject == null){
            synchronized (SingletonObject.class){
                if(singletonObject == null){
                    singletonObject = new SingletonObject();
                }
            }
        }
        return singletonObject;
    }
}
