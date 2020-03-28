package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FilterSimulation {

    public static void main(String[] args) {
        //filterFlux();
        //ignoreElementsFlux();
        //take();
        //takeLast();
        takeUntil(); // nao rola
    }

    public static void takeUntil(){
        var startCommand = Flux.just(1);
        var stopCommand = Flux.just(4);

        var streamOfData = Flux.just(1,2,3,4,5,6,7,8,9);

        streamOfData.skipUntilOther(startCommand)
                .takeUntilOther(stopCommand)
                .subscribe(System.out::println);

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void filterFlux(){
        var stream = Flux.just(1,2,3,4,5).filter(p -> p.equals(1)).subscribe(System.out::println);
    }

    public static void ignoreElementsFlux(){
        var elements = Flux.just(1,2,3,4,5).ignoreElements().subscribe(System.out::println);
    }

    public static void take(){
        var result = Flux.just(1,2,3,4).take(2).subscribe(System.out::println); //retorna os 2 primeiros
    }

    public static void takeLast(){
        Flux.just(1,2,3,4,5).takeLast(2).subscribe(System.out::println); //retorna os ultimos
    }
}
