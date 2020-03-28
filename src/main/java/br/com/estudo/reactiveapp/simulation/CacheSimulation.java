package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class CacheSimulation {

    public static void main(String[] args) throws InterruptedException {
        Flux<Integer> source = Flux.range(0,2)
                .doOnSubscribe(s -> System.out.println("nova inscrição,produtor frio.")); //doOnSubscribe emite um evento antes de cada inscrição

        Flux<Integer> cacheSource = source.cache(Duration.ofSeconds(1)); // o cache vai durar 1 segundo
        cacheSource.subscribe(e -> System.out.println("V1 onNext " + e));
        cacheSource.subscribe(e -> System.out.println("V2 onNext " + e));
        //os 2 inscritos acima, compartilham o mesmo dado já o inscrito abaixo, recebe o novo pois o cache ja expirou.

        Thread.sleep(1200);

        cacheSource.subscribe(e -> System.out.println("V3 onNext " + e));
    }
}
