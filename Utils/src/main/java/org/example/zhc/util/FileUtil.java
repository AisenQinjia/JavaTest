package org.example.zhc.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FileUtil {
    public static String readResourceFile(String path){
        StringBuilder textBuilder = new StringBuilder();
        try (InputStream in =  Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                Reader reader = new BufferedReader(new InputStreamReader
                (in, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }catch (IOException e){
            Log.info("exception"+e.getMessage());
        }
        return textBuilder.toString();
    }
    //获取文本文件内容
    public static String readCharacterFileToStr(String pathName,boolean isResource) throws IOException {
        InputStream fileInputStream = isResource? Thread.currentThread().getContextClassLoader().getResourceAsStream(pathName):new FileInputStream(pathName);
        byte[] bytes =new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        fileInputStream.close();
        return new String(bytes, StandardCharsets.UTF_8);
    }
    public static Properties readFileAsProperties(String pathName,boolean isResource) throws IOException {
        Properties properties = new Properties();
        InputStream fileInputStream = isResource? Thread.currentThread().getContextClassLoader().getResourceAsStream(pathName):new FileInputStream(pathName);
        properties.load(new InputStreamReader(fileInputStream));
        return properties;
    }
    public static void write2File(String pathName, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathName,true),"UTF-8"));
        PrintWriter out = new PrintWriter(writer);
        byte [] bs = { (byte)0xEF, (byte)0xBB, (byte)0xBF};
        out.write(new String(bs));
        out.write(content);
        writer.close();
        out.close();
    }

    public static BufferedReader reader(String pathName) throws FileNotFoundException {
        return new BufferedReader(new FileReader(new File(pathName)));
    }

}
