package org.example.zhc.resource;

import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GetResourceApp {
    public static void main(String[] args){
        InputStream url = GetResourceApp.class.getResourceAsStream("test");
        System.out.println("url "+url);
    }
    @Test
    public void readTextFile() throws IOException {
        InputStream fileReader = new FileInputStream("./test");
        byte[] bytes =new byte[fileReader.available()];
        fileReader.read(bytes);
        String config =new String(bytes, StandardCharsets.UTF_8);
        System.out.println("config: "+ config);
    }
}
