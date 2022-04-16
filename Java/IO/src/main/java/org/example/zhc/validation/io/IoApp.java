package org.example.zhc.validation.io;

import lombok.ToString;
import org.junit.Test;

import java.io.*;

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
    @Test
    public void fileWriter() throws IOException {
        //覆盖模式
        FileWriter fileWriter=new FileWriter("test");
        //追加模式
        FileWriter fileWriter2 =new FileWriter("test", true);
        fileWriter.write("bb");
        fileWriter.write("cc");

//        fileWriter.append("aa");
        fileWriter.flush();
        fileWriter.close();
    }
}
