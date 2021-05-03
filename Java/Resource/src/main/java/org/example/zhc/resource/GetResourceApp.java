package org.example.zhc.resource;

import java.io.InputStream;

public class GetResourceApp {
    public static void main(String[] args){
        InputStream url = GetResourceApp.class.getResourceAsStream("test");
        System.out.println("url "+url);
    }
}
