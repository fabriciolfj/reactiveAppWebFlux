package br.com.estudo.reactiveapp.simulation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.function.Predicate;

public class ReducingSimulation {

    private static final Logger log = LoggerFactory.getLogger(ReducingSimulation.class);

    public static void main(String[] args) {
        //new ReducingSimulation().any();
        //sum();
        //scan();
        //count();
        //exemplo();
        then();
    }

    public static void then(){ //ele recebe apenas 4 e 5, embora 1 2 3 sejam processados pelo flux
        Flux.just(1,2,3)
                .thenMany(Flux.just(4,5))
                //.thenEmpty(Flux.empty())
                .subscribe(System.out::println);

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void exemplo(){
        int bucketSize = 5;
        Flux.range(1,500).index()
                .scan(new int[bucketSize], (acc, elem) -> {
                    acc[(int) (elem.getT1() % bucketSize)] = elem.getT2();
                    return acc;
                }).skip(bucketSize)
                .map(array -> Arrays.stream(array).sum() * 1.0 / bucketSize)
        .subscribe(System.out::println);
    }

    public static void count(){
        Flux.just(1,2,3,4,5).count().subscribe(System.out::println);
    }

    public void any(){
        /*var result = Flux.just(3,5,7,9,11,15,16,17, 10, 2,4, 8)
                .any(e -> e % 2 == 0);

        result.subscribe(hashEvents -> System.out.println(hashEvents));*/
        Flux.just(3, 5, 7, 9, 11, 15, 16, 17)
                .any(e -> e % 2 != 0)
                .subscribe(hasEvens -> log.info("Has evens: {}", hasEvens));

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void sum(){ //reduce produz um unico resultado
        Flux.range(1,4).reduce(0, (acc, elem) -> acc + elem)
                .subscribe(System.out::println);
    }

    public static void scan(){ //vai lançando os resultados
        Flux.range(1,4).scan(0, (acc, elem) -> acc + elem)
                .subscribe(System.out::println);
    }
}
