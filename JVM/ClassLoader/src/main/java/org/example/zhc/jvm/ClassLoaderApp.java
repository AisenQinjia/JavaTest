package org.example.zhc.jvm;

import org.example.Log;

import java.util.ArrayList;

/**
 *
 */
public class ClassLoaderApp {
    public static void main(String[] args){
        ClassLoader appCl = ClassLoaderApp.class.getClassLoader();
        Log.info("app classloader: %s",appCl);
        Log.info("extension classLoader: %s",appCl.getParent());
        Log.info("bootstrap classLoader: %s", appCl.getParent().getParent());
        Log.info("context classLoader: %s", Thread.currentThread().getContextClassLoader());
    }
}
