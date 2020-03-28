package br.com.estudo.reactiveapp.simulation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class FlatMapSimulation {

    private final Logger log = LoggerFactory.getLogger(FlatMapSimulation.class);
    private Random random = new Random();
    public static void main(String[] args) {
        execute();
        //exemplo();
        //testSimples();
        //new FlatMapSimulation().requestBooksUser();
    }

    public void requestBooksUser(){
        Flux.just("user-1", "user-2", "user-3")
                .flatMap(u -> requestBooks(u)
                //.flatMapSequential(u -> requestBooks(u)
                //.map(u -> requestBooks(u) //map não atua com retorno flux
                        .map(b -> u + "/" + b))
                .subscribe(r -> log.info("onNext: {}", r));

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Flux<String> requestBooks(String user){
        return Flux.range(1, 5)                      // (1)
                .map(i -> "book-" + i)                                // (2)
                .delayElements(Duration.ofMillis(3));                 // (3)
    }

    private static void testSimples(){
        Flux<Integer> dados = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5));
        dados.flatMap(a -> Mono.just(a)).subscribe(System.out::println);
    }

    private static void execute() {
        Flux<Integer> dados = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5));
        dados.flatMap(a -> Mono.just(a).subscribeOn(Schedulers.parallel()))
                //.doOnNext(a -> System.out.println("Valor recebido: " + a))
            .subscribe(System.out::println);

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public  static void exemplo() {
        Flux<Integer> f = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5));
        f.flatMap(a -> Mono.just(a).subscribeOn(Schedulers.parallel()))
                .doOnNext(
                        a ->
                                System.out.println(
                                        "Received: " + a + " on thread: " + Thread.currentThread().getName()))
                /*.flatMap(
                        a -> {
                            System.out.println(
                                    "Received in flatMap: " + a + " on thread: " + Thread.currentThread().getName());
                            a++;
                            return Mono.just(a).subscribeOn(Schedulers.elastic());
                        })*/
                .subscribe(
                        a ->
                                System.out.println(
                                        "Received (in the subscriber): "
                                                + a
                                                + " on thread: "
                                                + Thread.currentThread().getName()));
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
