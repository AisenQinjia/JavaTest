package org.example.zhc.jvm;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 自定义classLoader
 * 不向上代理
 */
public class PandoraClassLoader extends URLClassLoader {

    public PandoraClassLoader(URL[] urls){
        super(urls,Thread.currentThread().getContextClassLoader());
    }

    public static final Object loadLock = new Object();

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> z = loadClass(name,false);
        if(z == null){
            return super.loadClass(name);
        }else{
            return z;
        }
    }

    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (loadLock){
            Class<?> c = findClass(name);
            if(resolve){
                resolveClass(c);
            }
            return c;
        }
    }
}
