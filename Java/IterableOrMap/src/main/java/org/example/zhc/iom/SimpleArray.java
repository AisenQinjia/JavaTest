package org.example.zhc.iom;

import java.util.Iterator;

/**
 * 一个简单的iterable实现
 * @author aisen
 */
public class SimpleArray<T> implements Iterable<T> {
    private T[] elements;

    public SimpleArray(){
//        elements = new
    }
    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<T>{
        private int currentIndex;
        @Override
        public boolean hasNext() {
            return currentIndex < elements.length;
        }

        @Override
        public T next() {
            T e = elements[currentIndex];
            currentIndex++;
            return e;
        }
    }
}

