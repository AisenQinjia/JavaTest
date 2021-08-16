package org.example.zhc.jvm;

import org.example.Log;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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

    @Test
    public void loadDifferVersion() throws ClassNotFoundException, MalformedURLException {
        printVersion(String.class);
        String classV1Path = "file:\\E:\\AppData\\maven\\repo\\com\\fasterxml\\jackson\\core\\jackson-core\\2.11.2\\jackson-core-2.11.2.jar";
        String classV2Path = "file:\\E:\\AppData\\maven\\repo\\com\\fasterxml\\jackson\\core\\jackson-core\\2.9.10\\jackson-core-2.9.10.jar";
        String className = "com.fasterxml.jackson.core.type.ResolvedType";
        URLClassLoader clsLoader = URLClassLoader.newInstance(new URL[] {new URL(classV1Path)});
        Class cls = clsLoader.loadClass(className);
        printVersion(cls);
        URLClassLoader clsLoader2 = URLClassLoader.newInstance(new URL[] {new URL(classV2Path)});
        Class cls2 = clsLoader2.loadClass(className);
        printVersion(cls2);
    }

    private void printVersion(Class<?> clazz){
        Package p = clazz.getPackage();
        System.out.printf("%s%n  Title: %s%n  Version: %s%n  Vendor: %s%n",
                clazz.getName(),
                p.getImplementationTitle(),
                p.getImplementationVersion(),
                p.getImplementationVendor());
    }
}
