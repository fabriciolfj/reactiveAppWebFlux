package br.com.estudo.reactiveapp.simulation;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class StreamsSimulation {

    public static void main(String[] args)
    {
        var  stream = Stream.of(1,2,3,4,5,6,7,8,9)
                .collect(toList());;
        stream.forEach(p -> System.out.println(p));
    }
}
