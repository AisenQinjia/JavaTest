package org.zhc.lsp;

import org.example.zhc.util.Log;

import java.util.HashMap;
import java.util.Map;

class Parent{
    public void doSomething(Map map){
        Log.info("Parent do something");
    }
}
//class Child extends Parent{
////    public void doSomething(HashMap map){
////        Log.info("Child do something1");
////    }
//
////    @Override
////    public void doSomething(Map map){
////        Log.info("Child do something2");
////    }
//}
public class LspApp {
    public static void main(String[] args){
        Parent child = new Parent();
        child.doSomething(new HashMap());
    }
}
