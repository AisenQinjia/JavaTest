package org.example.zhc.util.zhc.validation.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * reactive stream test
 */
public class ReactorApp {
    @Test
    public void flux() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        Flux.fromIterable(integerList)
                .subscribe(System.out::println);
    }

    @Test
    public void mono_create(){
        Mono.just("so what")
                .subscribe(System.out::println);
    }

    @Test
    public void mono_onNext() {
        Mono.create(monoSink -> {
            System.out.println("aaa");
            monoSink.success("success");
        }).doOnNext(o -> {
            System.out.println("bbb " + o.getClass().getSimpleName());
        }).doOnNext(o->{
            System.out.println("ccc");
        }).subscribe(System.out::println);
    }

    @Test
    public void mono_nextEmit(){
        Mono.just("hi")
                .doOnNext(System.out::println)
                .flatMap(str-> Mono.just(1))
                .subscribe(System.out::println);

    }

    @Test
    public void fluxTest(){
        List<Integer> ints = new ArrayList<>();
        //从1开始，递增1，共10个
        for (int i = 1; i <= 10; i++) {
            ints.add(i);
        }
        Flux.fromIterable(ints)
                .doOnNext(System.out::println)
                .flatMap(i->{
                    if(i%2==0){
                        return Mono.just(i);
                    }else{
                        return Mono.empty();
                    }
                }).flatMap(i->{
                    if(i%4==0){
                        return Mono.just(i);
                    }else{
                        return Mono.empty();
                    }
                })
                .collectList()
                .subscribe(System.out::println);
    }

}
