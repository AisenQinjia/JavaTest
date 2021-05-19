package org.example;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
}
