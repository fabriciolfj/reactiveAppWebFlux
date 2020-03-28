package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;

public class CombiningStreamsSimulation {

    public static void main(String[] args) {
        System.out.println("== concat ==");
        concat();// concatena todas as fontes (consome o primeiro e envia ao assinante, e faz o mesmo com o segundo)
        System.out.println("== merge ==");
        merge(); //junta os 2 fluxos
        System.out.println("== zip ==");
        zip(); //combina os elementos(com base em uma funcao específica) e emite essas combinações

    }

    public static void concat(){
        Flux.concat(Flux.just(1,2), Flux.just(3,4)).subscribe(System.out::println);
    }

    public static void merge(){
        Flux.merge(Flux.just(1,2), Flux.just(3,4)).subscribe(System.out::println);
    }

    public static void zip(){
        Flux.zip(Flux.just(1,2), Flux.just(3,4)).subscribe(System.out::println);
    }

}
