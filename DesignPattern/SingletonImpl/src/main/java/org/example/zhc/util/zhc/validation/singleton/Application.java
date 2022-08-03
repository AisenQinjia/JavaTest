package org.example.zhc.util.zhc.validation.singleton;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;

@Slf4j
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class);
        log.info("The name is: {}", FieldSingleton.FIELD_SINGLETON.getMyName());
        log.info("The name is: {}", MethodSingleton.getSingleton().getMyName());
        log.info("The name is: {}", EnumSingleton.INSTANCE.getMyName());

        tempTest();
    }

    private interface FunctionInterface{
        int sum(int x,int y);
    }
    private static void passinterface(FunctionInterface fi){
        log.info("sum 1 + 2: {}",fi.sum(1,2));
    }
    public static int sum(int x, int y){
        return x+y;
    }
    public static void tempTest(){
        log.info("max: {}", Integer.MAX_VALUE);
        log.info("max > -max: {}", Integer.MAX_VALUE > -Integer.MAX_VALUE);
        log.info("(max - -max )> 0 : {}", (Integer.MAX_VALUE - -Integer.MAX_VALUE)> 0);

        //lambda expression and functional interface
        int freeA = 1;
        Integer freeObjA = 1;
//        int x = 1;
        FunctionInterface fi = (x,y)-> x+y + freeObjA;
        val freeAA = freeA + 1;
        passinterface(Application::sum);
    }
}
