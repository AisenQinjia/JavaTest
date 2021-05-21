package org.example.zhc.iom;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 一个简易的mp实现
 * 数组加链表的方式
 * @param <K>
 * @param <V>
 */
public class SimpleMap<K,V> implements Map<K,V> {
    Node<K,V>[] nodes;
    final int initialCapacity = 16;
    public SimpleMap(){
        nodes = (Node<K,V>[])new Node[initialCapacity];
    }
    int size;
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Node<K,V> currentNode = null;
        for(Node<K,V> node: nodes){
            currentNode = node;
            if(currentNode != null){
                do{
                    if(currentNode.value.equals(value)){
                        return true;
                    }else{
                        currentNode = currentNode.nextNode;
                    }
                }while (currentNode != null);
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int index = getIndex(key);
        Node<K,V> node = nodes[index];
        if(node != null){
           return getInList(node,key);
        }else{
            return null;
        }
    }
    private int hash(Object key){
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    private int getIndex(Object key){
        return hash(key)%nodes.length;
    }
    private V getInList(Node<K,V> node, Object key){
        Node<K,V> currentNode = node;
        do{
            if(currentNode.key.equals(key)){
                return currentNode.value;
            }
        }while (currentNode != null);
        return null;
    }
    private V putInList(@NotNull Node<K,V> headNode, Node<K,V> toAddNode){
        Node<K,V> currentNode = headNode;
        Node<K,V> prevNode = null;
        while (currentNode != null ){
            if(currentNode.key.equals(toAddNode.key)){
                V oldVal = currentNode.value;
                currentNode.value = toAddNode.value;
                return oldVal;
            }else {
                prevNode = currentNode;
                currentNode = currentNode.nextNode;
            }
        }
        prevNode.nextNode = toAddNode;
        size++;
        return null;
    }

    @Override
    public V put(K key, V value) {
        Node<K,V> node = new Node<>(key,value);
        int index = getIndex(key);
        if(nodes[index] == null){
            nodes[index] = node;
            size++;
            return null;
        }else{
         V oldVale =  putInList(nodes[index],node);
         if(oldVale == null){
             size++;
         }
         return oldVale;
        }
    }

    @Override
    public V remove(Object key) {
        int index = getIndex(key);
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    final class Node<K,V>{
        K key;
        V value;
        Node<K,V> nextNode;

        public Node(K key,V value){
            this.key = key;
            this.value = value;
        }
    }
}
