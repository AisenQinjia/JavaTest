package org.example.zhc.jvm;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 自定义classLoader
 * 不向上代理
 */
public class PandoraClassLoader extends URLClassLoader {

    public PandoraClassLoader(URL[] urls){
        super(urls,null);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try
        {
            return findClass(name);
        }
        catch( ClassNotFoundException e )
        {
            return super.loadClass(name, resolve);
        }
    }
}
