package org.example.zhc.validation.resource;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GetResourceApp {
    public static void main(String[] args){
        //url not null running in idea,null running in power shell
        //the path separator mush be `/`, the ClassLoader.getResource doc says so.
        InputStream url = GetResourceApp.class.getResourceAsStream("/dir/test");
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
