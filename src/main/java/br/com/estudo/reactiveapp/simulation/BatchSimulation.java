package br.com.estudo.reactiveapp.simulation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.LinkedList;

public class BatchSimulation {

    private final Logger log = LoggerFactory.getLogger(BatchSimulation.class);

    public static void main(String[] args) {
        //buffer();//vou consumindo o fluxo que está sendo controlado pelo bufer, como se fosse um amortecedor de dados, ele acumula os dados depois emite
        //new BatchSimulation().window(); // gera subfluxos, vai emitindo os dados conforme vai chegando, mas a quebra da janela e diante uma condição que deve-se implementar
        new BatchSimulation().groupBy();
    }

    public  void window(){
        Flux<Flux<Integer>> windowedFlux = Flux.range(101,20).windowUntil(this::isPrime, true); //windonUntil define se cortamos a janel antes ou do do predicate
        windowedFlux.subscribe(w -> // pega Mono<List<Integer>> para cada flux<List<Integer>>
                w.collectList().subscribe(System.out::println));//cada array e uma janela, a primera vem vazia, pois quando inicia ele gera uma janela inicial que no caso nao tem dados
    }

    public void groupBy(){//grupando por dados impares e pares
        var result = Flux.range(1,7)
                .groupBy(e -> e % 2 == 0 ? "Par" : "Impar"); //retorna Flux<GroupedFlux<String, Integer>>

        result.subscribe(groupFlux -> groupFlux
                .scan(
                new LinkedList<>(),
                (list, elem) -> {
                    list.add(elem);
                    if(list.size() >2){
                        list.remove(0);
                    }

                    return list;
                })
                .filter(arr -> !arr.isEmpty())
                .subscribe(data -> System.out.println("data- " + groupFlux.key() + " " + data)));

        /*Flux.range(1, 7)                                                   // (1)
                .groupBy(e -> e % 2 == 0 ? "Even" : "Odd")                     // (2)
                .subscribe(groupFlux -> groupFlux                              // (3)
                        .scan(                                                     // (4)
                                new LinkedList<>(),                                    // (4.1)
                                (list, elem) -> {
                                    list.add(elem);                                    // (4.2)
                                    if (list.size() > 2) {
                                        list.remove(0);                                // (4.3)
                                    }
                                    return list;
                                })
                        .filter(arr -> !arr.isEmpty())                             // (5)
                        .subscribe(data ->                                         // (6)
                                log.info("{}: {}", groupFlux.key(), data)));*/
    }

    public boolean isPrime(long number) {
        number = Math.abs(number);
        if (number % 2 == 0) {
            return false;
        }
        for (long i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void buffer(){
        Flux.range(1,13).buffer(4).subscribe(System.out::println);
    }
}
