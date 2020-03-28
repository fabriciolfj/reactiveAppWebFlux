package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class DealingTimeSimulation {

    public static void main(String[] args) throws InterruptedException {
        Flux.range(0,5)
                .delayElements(Duration.ofMillis(100))
                .elapsed()  //emito um evento a cada 100 milisegundos, mas cuidado, não e preciso
                .subscribe(s -> System.out.println("Milisegundos: " + s.getT1() + ", evento: " + s.getT2()));

        Thread.sleep(1000);
    }
}
