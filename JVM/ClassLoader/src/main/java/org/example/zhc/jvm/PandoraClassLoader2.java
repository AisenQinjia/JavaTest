package org.example.zhc.jvm;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * 自定义classLoader
 * 不向上代理
 */
public class PandoraClassLoader2 extends ClassLoader {

    private ChildClassLoader childClassLoader;

    public PandoraClassLoader2(URL[] urls)
    {
        super(Thread.currentThread().getContextClassLoader());
        childClassLoader = new ChildClassLoader( urls, new DetectClass(this.getParent()) );
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        try
        {
            return childClassLoader.findClass(name);
        }
        catch( ClassNotFoundException e )
        {
            return super.loadClass(name, resolve);
        }
    }

    private static class ChildClassLoader extends URLClassLoader
    {
        private DetectClass realParent;

        public ChildClassLoader( URL[] urls, DetectClass realParent )
        {
            super(urls, null);
            this.realParent = realParent;
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException
        {
            try
            {
                Class<?> loaded = super.findLoadedClass(name);
                if( loaded != null )
                    return loaded;
                return super.findClass(name);
            }
            catch( ClassNotFoundException e )
            {
                return realParent.loadClass(name);
            }
        }
    }

    private static class DetectClass extends ClassLoader
    {
        public DetectClass(ClassLoader parent)
        {
            super(parent);
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException
        {
            return super.findClass(name);
        }
    }
}
