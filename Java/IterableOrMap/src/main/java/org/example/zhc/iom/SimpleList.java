package org.example.zhc.iom;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 一个简单的list实现
 * @author aisen
 */
public class SimpleList<T> implements List<T> {
    private Object[] elements;
    private int size;
    public SimpleList(){
        elements  = new Object[0];
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return elements.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        for(Object e:elements){
            if(e.equals(o)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        Object[] objects = new Object[elements.length];
        for(int i = 0;i<elements.length;i++){
            objects[i] = elements[i];
        }
        return objects;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if(a.length <= elements.length){
            return  (T1[])toArray();
        }else {
            for(int i =0; i< elements.length;i++){
                a[i] = (T1) elements[i];
            }
            a[elements.length] = null;
            return a;
        }
    }

    @Override
    public boolean add(T t) {
        try {
            if(size < (elements.length -1)){
                elements[size + 1] = t;
                size++;
            }else{
                reSize(size()*2 + 1);
                add(t);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void reSize(int size){
        Object[] newEs = new Object[size];
        for(int i = 0;i<elements.length;i++){
            newEs[i] = elements[i];
        }
        elements = newEs;
    }

    @Override
    public boolean remove(Object o) {
        for(int i =0;i<elements.length;i++){
            if(o.equals(elements[i])){
                elements[i] = null;
                shift(i+1,size,-1);
                size--;
                return true;
            }
        }
        return false;
    }

    private void shift(int headIndex,int tailIndex, int moveCount){
        if(moveCount == 0
                || elements.length == 0
                || headIndex >= elements.length
                || tailIndex >= elements.length
                || headIndex > tailIndex){
            return;
        }
        //左移
        if(moveCount < 0){
            moveCount = elements.length + moveCount;
        }
        for(;headIndex<= tailIndex;headIndex++){
            int index = (headIndex + moveCount)%moveCount;
            elements[index] = elements[headIndex];
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public void clear() {

    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public T set(int index, T element) {
        return null;
    }

    @Override
    public void add(int index, T element) {

    }

    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    private class Itr implements Iterator<T>{
        private int currentIndex;
        @Override
        public boolean hasNext() {
            return currentIndex < elements.length;
        }

        @Override
        public T next() {
            T e = (T)elements[currentIndex];
            currentIndex++;
            return e;
        }
    }
}

