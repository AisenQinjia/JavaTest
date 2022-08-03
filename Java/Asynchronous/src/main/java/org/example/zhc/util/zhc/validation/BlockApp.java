package org.example.zhc.util.zhc.validation;

import org.junit.jupiter.api.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.boot.origin.SystemEnvironmentOrigin;

import java.io.IOException;
import java.io.InputStream;

public class BlockApp {

    public static void main(String[] args) throws IOException {
        InputStream in = System.in;
        int result = in.read();
        System.out.println("result "+ result);
    }
}
