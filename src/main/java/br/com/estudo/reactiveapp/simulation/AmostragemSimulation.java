package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class AmostragemSimulation {

    public static void main(String[] args) {
        new AmostragemSimulation().sample();//consumir por amostragem
    }

    public void sample(){
        Flux.range(1,100)
                .delayElements(Duration.ofMillis(1))
                .sample(Duration.ofMillis(20)) //amostra com base no timeout de 20 milisegundos\
                .subscribe(System.out::println);

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
