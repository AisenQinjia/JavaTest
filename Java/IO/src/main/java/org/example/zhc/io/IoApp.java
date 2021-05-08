package org.example.zhc.io;

import lombok.ToString;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class IoApp {
    public static void main(String[] args){

    }
    @Test
    public void fileObj(){
        File f1 = new File("foo");
        System.out.println(f1.getAbsolutePath());
        System.out.println(f1.exists() && f1.canRead());
    }
    @Test
    public void fileReader() throws FileNotFoundException {
        FileReader fr = new FileReader("/test");

    }
}
