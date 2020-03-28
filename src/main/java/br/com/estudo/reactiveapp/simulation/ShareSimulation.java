package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class ShareSimulation {

    public static void main(String[] args) throws InterruptedException {
        Flux<Integer> source = Flux.just(1,2,3,4,5)
                .delayElements(Duration.ofMillis(100))
                .doOnSubscribe(s -> System.out.println("nova inscrição cold publisher"));

        Flux<Integer> cachedSource = source.share(); //compartilhando o evento
        cachedSource.subscribe(s -> System.out.println("V1 onNext: " + s)); // o primeiro inscrito chegou a receber os dados, mas devido ao sleep abaixo,
        // o segundo inscrito não recebeu, pois primeiro ja pegou todos

        Thread.sleep(1000);

        cachedSource.subscribe(s -> System.out.println("V2 onNext: " + s));
    }
}
