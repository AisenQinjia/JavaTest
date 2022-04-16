package org.example.zhc.validation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerApp {
    public static final int port = 5000;
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("waiting for connection");
            Socket clientSocket = serverSocket.accept();
            System.out.println("a new connection");
            PrintWriter out =  new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String hello = in.readLine();
            System.out.println("client: "+hello);
            out.println("server hello back!");
            out.flush();
        }catch (Exception e){

        }
    }
}
