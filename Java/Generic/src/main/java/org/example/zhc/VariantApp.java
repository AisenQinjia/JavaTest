package org.example.zhc;

import org.example.Log;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Fruit {}
class Apple extends Fruit {}
class Jonathan extends Apple {}
class Orange extends Fruit {}

public class VariantApp {
    public static void main(String[] args) {

    }
    @Test
    public void arrayVariance(){
        Fruit[] fruit = new Apple[10];

        fruit[0] = new Apple();
        fruit[1] = new Jonathan();
        try {
            fruit[0] = new Fruit();
        } catch (Exception e) {
            Log.info("111");
            System.out.println(e);
        }
        try {
            fruit[0] = new Orange();
        } catch (Exception e) {
            Log.info("222");
            System.out.println(e);
        }
    }

    @Test
    public void interfaceVariance(){
        //不变
        List<Fruit> invariance = new ArrayList<Fruit>();
        //协变
        List<? extends Fruit> covariance = new ArrayList<Apple>();
        //逆变
        List<? super Apple> contravariance = new ArrayList<Fruit>();
    }

    private void ivt(List<? super Apple> fs){
        fs.add(new Apple());
        fs.add(new Jonathan());
    }

    @Test
    public void typeWildcards(){
        List<String> nums = new ArrayList<>();

    }
}