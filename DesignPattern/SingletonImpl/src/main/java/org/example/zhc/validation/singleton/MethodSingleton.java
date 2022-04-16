package org.example.zhc.validation.singleton;

public class MethodSingleton implements SSingleton , Comparable<MethodSingleton>{
    private final static MethodSingleton SINGLETON = new MethodSingleton();
    private int position;
    private static int th = 0;
    private MethodSingleton(){
        position = th++;
    }
    public static MethodSingleton getSingleton(){
        return SINGLETON;
    }

    @Override
    public String getMyName() {
        return getClass().getSimpleName();
    }

    @Override
    public int compareTo(MethodSingleton o) {
        return Double.compare(o.position,position);
    }
}
