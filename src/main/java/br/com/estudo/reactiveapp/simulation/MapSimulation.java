package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;

import java.time.Instant;

public class MapSimulation {

    public static void main(String[] args) {
        Flux.range(2018, 5)
                .timestamp()
                .index()
                .subscribe(
                        e -> System.out.println(
                                "index: " + e.getT1() + ", ts: " + Instant.ofEpochMilli(e.getT2().getT1()) + ", value: "+ e.getT2().getT2()));
    }
}
