package org.example.zhc.util.zhc.validation.reflection.ann;
@EntityAbility(abilityClasses = {Entity.class})
public class Entity {

    public static void main(String[] args){
        EntityAbility ann = Entity.class.getAnnotation(EntityAbility.class);
        long a = 1100;
        long b = 1000;
        double ceil = Math.ceil((float)a/ b);
        System.out.println(ceil);
    }
}
