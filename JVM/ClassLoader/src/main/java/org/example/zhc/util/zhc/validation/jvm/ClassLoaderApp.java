package org.example.zhc.util.zhc.validation.jvm;

import org.example.zhc.util.Log;
import org.junit.jupiter.api.Test;

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

    String classV1Path = "file:\\E:\\AppData\\maven\\repo\\com\\fasterxml\\jackson\\core\\jackson-core\\2.11.2\\jackson-core-2.11.2.jar";
    String classV2Path = "file:\\E:\\AppData\\maven\\repo\\com\\fasterxml\\jackson\\core\\jackson-core\\2.9.10\\jackson-core-2.9.10.jar";
    String classV3Paht = "file:\\E:\\AppData\\maven\\repo\\com\\fasterxml\\woodstox\\woodstox-core\\5.3.0\\woodstox-core-5.3.0.jar";
    String className = "com.fasterxml.jackson.core.type.ResolvedType";
    String class3Name = "com.ctc.wstx.util.ArgUtil";

    @Test
    public void loadDifferVersion() throws ClassNotFoundException, MalformedURLException {
        URLClassLoader clsLoader = URLClassLoader.newInstance(new URL[] {new URL(classV1Path)});
        Class cls = clsLoader.loadClass(className);
        printVersion(cls);
        URLClassLoader clsLoader2 = URLClassLoader.newInstance(new URL[] {new URL(classV2Path)});
        Class cls2 = clsLoader2.loadClass(className);
        printVersion(cls2);
        //但是如果paren, 由于委托机制将不会load到这个version的类
        URLClassLoader clsLoader3 = URLClassLoader.newInstance(new URL[] {new URL(classV3Paht)});
        Class cls3 = clsLoader3.loadClass(class3Name);
        printVersion(cls3);
    }

    @Test
    public void pandoraClassLoader() throws MalformedURLException, ClassNotFoundException {
        PandoraClassLoader classLoader = new PandoraClassLoader(new URL[]{new URL(classV3Paht)});
        Class cls = classLoader.loadClass(class3Name);
        printVersion(cls);
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
