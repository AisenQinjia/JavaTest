package org.exmaple.zhc.test;

import org.example.zhc.SocketServerApp;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
    @Test
    public void greetServer() throws IOException {
        Socket socket = new Socket("127.0.0.1", SocketServerApp.port);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("hello from client!");
        out.flush();
        String ret = in.readLine();

        System.out.println("ret from server: " +ret);
    }
}
