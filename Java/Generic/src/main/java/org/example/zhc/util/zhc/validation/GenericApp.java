package org.example.zhc.util.zhc.validation;

import lombok.var;
import org.example.zhc.util.Log;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.UnaryOperator;

interface Factory<T>{
    T create();
}

class F1 implements Factory<String>{

    @Override
    public String create() {
        return "str";
    }
}
class Widget{
    public static class F2 implements Factory<Integer>{
        @Override
        public Integer create() {
            return new Integer(1);
        }
    }
}

class Foo<T>{
    T item;
    public <F extends Factory<T>> Foo(F factory){
        this.item = factory.create();
    }
}
public class GenericApp {
    @Test
    public void erasure(){
        Class a = new ArrayList<Integer>().getClass();
        Class b = new ArrayList<String>().getClass();
        Log.info("a==b: %s",a==b);
    }

    @Test
    public void getMyType(){
        String a = "1";

        GenericClass<String> genericClass = new GenericClass<>(String.class);

//        GenericClass<String> genericClass2 = new GenericClass<String>(a.getClass());
    }

    @Test
    public void setCompare(){
        Set<String> stringSet = new HashSet<>();
        stringSet.add("1");
        ((Set)stringSet).add(1);
        ((Set)stringSet).add("1");
        stringSet.forEach(s -> {});
        String a = getT();
        Integer b = getT();
        var c = this.<GenericApp>getT();


    }

    @Test
    public void typeToken(){
//        TypeToken
    }

    //Generic singleton factory pattern
    private static final UnaryOperator<Object> IDENTITY_FN = (t) -> t;

    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identityFunction() {
        return (UnaryOperator<T>) IDENTITY_FN;
    }
    @Test
    public void UnaryOperator(){
        //no compile error
        UnaryOperator<String> a  = identityFunction();

        UnaryOperator<Object> IDENTITY_FN2 = (t) -> t;
        //compile error: can not cast UnaryOperator<Object> to UnaryOperator<String> ?
//        UnaryOperator<String> b = (UnaryOperator<String>)IDENTITY_FN2;
    }
    @Test
    public void immutable() {
        Set a = new HashSet<String>();
        Set<Integer> b = (Set<Integer>) a;
        Collections.emptySet();
        Set<Object> stringSet = this.union(new HashSet<Integer>(),new HashSet<Long>());
    }

    @SuppressWarnings("unchecked")
    public <T> T getT(){
        return (T)"a";
    }

    public <E> Set<E> union(Set<? extends E> set1,Set<? extends E> set2){
        Set<E> u = new HashSet<>(set1);
        u.addAll(set2);
        return u;
    }

    public void max(){
//        ComparableImplParent n1 =  this.<ComparableImplParent>max1(new ArrayList<ComparableImplChild>());
//        ComparableImplChild n2 =  this.max2(new ArrayList<ComparableImplChild>());
//        ComparableImplChild n3 =  this.max3(new ArrayList<ComparableImplChild>());
//        ComparableImplParent n4 =  this.max3(new ArrayList<ComparableImplChild>());
//        ScheduledFuture<?> n5 = this.max2(new ArrayList<ScheduledFuture<Object>>());
//        ScheduledFuture<?> n6 = this.max3(new ArrayList<ScheduledFuture<Object>>());
    }
    public <T extends Comparable<T>> T max1(List<T> ls){
        return null;
    }

    //T works only when extends Comparable directly
    public <T extends Comparable<T>> T max2(List<? extends T> ls){
        return null;
    }

    //T works whether extends Comparable directly or not
    public <T extends Comparable<? super T>> T max3(List<? extends T> ls){
        return null;
    }
    public void swap(){

    }

    public static <E> void swap1(List<E> list,int i,int j){

    }

    //better than swap1, less type parameter
    public static void swap2(List<?> list,int i,int j){
       swap3(list,i,j);
    }
    private static <E> void swap3(List<E> list,int i,int j){

    }
    static class ComparableImplParent implements Comparable<ComparableImplParent>{

        @Override
        public int compareTo(ComparableImplParent o) {
            return 0;
        }
    }

    static class ComparableImplChild extends ComparableImplParent {

    }
    @Test
    public void pickTwoTest(){
        //runtime cast error: cannot cast Object to String
//        String[] a = pickTwo("a","b","c");

        String[] b = toArray("a","b");
     }
    //not safe though...
    @SafeVarargs
    public static <T> T[] toArray(T... e){
        return e;
    }

    public static <T> T[] pickTwo(T a, T b, T c){
        return toArray(a,b);
    }
    public static <T> T[] boundedArray(List<? extends Number> a){
        return null;
    }

    private Map<Class<?>,Object> fMap = new HashMap<>();

    public <T> void putFavorite(Class<T> clazz,T inst){
        fMap.put(clazz,clazz.cast(inst));
    }

    public <T> T getFavorite(Class<T> clazz){
        return clazz.cast(fMap.get(clazz));
    }

    @Test
    public void heterogeneousContainer(){
//        Class<ArrayList> arrayListClass = ArrayList.class;
//        List<Integer> intList = new ArrayList<>();
//        Class<? extends List> aClass = intList.getClass();
//        Class<List> listClass = List.class;
//        Class<? extends ArrayList> aClass1 = new ArrayList<Integer>().getClass();
//        putFavorite(List.class,new ArrayList<Integer>());
//        List favorite = getFavorite(List.class);
//        List favorite1 = getFavorite(listClass);
//        ArrayList favorite2 = getFavorite(aClass1);

        putFavorite(ArrayList.class,new ArrayList<String>());
        ArrayList<Integer> favorite = getFavorite(ArrayList.class);
        favorite.add(1);
        ArrayList<String> favorite2 = getFavorite(ArrayList.class);
        favorite2.add("sdf");

//        List<Integer> integers = Collections.checkedList(new ArrayList<Integer>(), Integer.class);
//        integers.add(1);
//        putFavorite(List.class,integers);
//        List favorite = getFavorite(List.class);
//        favorite.add("dsfds");
    }

    @Test
    public void subClass(){
        List<Integer> i = new ArrayList<>();
        Class<? extends Integer> aClass = List.class.asSubclass(Integer.class);

    }
}
