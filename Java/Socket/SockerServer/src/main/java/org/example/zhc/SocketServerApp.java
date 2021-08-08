package org.example.zhc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerApp {
    public static final int port = 0;
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            clientSocket.getInputStream();
        }catch (Exception e){

        }
    }
}
