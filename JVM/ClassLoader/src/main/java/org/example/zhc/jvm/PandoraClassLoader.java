package org.example.zhc.jvm;

import java.net.URL;

/**
 * 自定义classLoader
 * 不向上代理
 */
public class PandoraClassLoader extends ClassLoader {

    public PandoraClassLoader(URL[] urls){

    }
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

    }


}
