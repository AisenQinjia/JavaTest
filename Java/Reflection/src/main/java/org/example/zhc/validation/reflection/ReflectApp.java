package org.example.zhc.validation.reflection;

import lombok.SneakyThrows;
import org.example.Log;
import org.example.zhc.validation.reflection.internal.Bird;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReflectApp {
    public static void main(String[] args){

    }
    @SneakyThrows
    @Test
    public void getAll(){
        Class reflectClass = Class.forName("org.example.zhc.validation.reflection.Class2Reflected");
        Class b = reflectClass.getClass();
//        Class reflectClass = new Class2Reflected().getClass();


        Field[] fields =  reflectClass.getDeclaredFields();
        for(Field field: fields){
            Log.info("field: %s, type: %s",field.getName(),field.getType());

        }
        Method[] methods = reflectClass.getDeclaredMethods();
        for(Method method:methods){
            Log.info("method: %s", method.getName());
        }
        Class[] classes = reflectClass.getInterfaces();
        for (Class clazz: classes){
            Log.info("interface: %s", clazz.getName());
        }

        String[] strings = new String[3];
        Class arrayClass = strings.getClass();
        Log.info("array class: %s",arrayClass.getName());
    }

    @Test
    public void isAnnotationPresent(){
        ServiceChild sc = new ServiceChild();
        System.out.println(String.format("serviceChild isAnnotationPresent OneAnnotation: %s",sc.getClass().isAnnotationPresent(OneAnnotation.class)));
    }

    @Test
    public void getInterfaces(){
        Bird bird = new Bird();
        printInterface(bird.getClass());
    }

    void printInterface(Class<?> clazz){
        Set<Class<?>> interfaces = getAllInterfaces(clazz);
        for (Class<?> anInterface : interfaces) {
            System.out.println("detected interfaces: " + anInterface.getName());
        }
    }

    public static Set<Class<?>> getAllInterfaces(Class<?> clazz){
        Class<?>[] parentInterfaces = clazz.getInterfaces();
        Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(parentInterfaces));
        for(Class<?> parentItf: parentInterfaces){
            interfaces.addAll(getAllInterfaces(parentItf));
        }
        if(clazz.getSuperclass() == null){
            return interfaces;
        }
        interfaces.addAll(getAllInterfaces(clazz.getSuperclass()));
        return interfaces;
    }
//    @Test
//    public void isAnnotationPresent2(){
//        ServiceChild sc = new ServiceChild();
//        System.out.println(String.format("serviceChild isAnnotationPresent OneAnnotation: %s",sc.getClass().isAnnotation(OneAnnotation.class)));
//    }
}
