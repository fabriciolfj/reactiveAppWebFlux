package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;

import java.util.Comparator;

public class CollectionSimulation {

    public static void main(String[] args) {
        //collectSortedList();
        distinct();
    }

    public static void collectSortedList(){
        Flux.just(8,5,6,2,3,1).collectSortedList(Comparator.reverseOrder())
                .subscribe(System.out::println);
    }

    public static void distinct(){
        Flux.just(1,2,3,4,5,2,3,46,7,3,1).distinct().subscribe(System.out::println);
    }
}
