package br.com.estudo.reactiveapp.simulation;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.Flow;

public class Transaction {

    public static final Random random = new Random();

    private final int id;

    public Transaction(int id){
        this.id = id;
        System.out.println("criado: " + id);
    }

    public static Mono<Transaction> beginTransaction(){
        return Mono.defer(() ->
            Mono.just(new Transaction(random.nextInt(1000)))
        );
    }

    public Flux<String> insertRows(Publisher<String> rows){
        return Flux.from(rows)
                .delayElements(Duration.ofMillis(100))
                .flatMap(r -> {
                    if(random.nextInt(10) < 2){
                        return Mono.error(new RuntimeException("Error: " + r));
                    }

                    return Mono.just(r);
                });
    }

    public Mono<Void> commit(){
        return Mono.defer(() -> { //defer ele é ocioso, ou seja, ele emite o evento depois de assinado, diferente do just que ja invoca o metodo ou evento vinculado.
            System.out.println("commit: " + id);

            if(random.nextBoolean()){
                return Mono.empty();
            }

            return Mono.error(new RuntimeException("Conflit"));
        });
    }

    public Mono<Void> rollback(){
        return Mono.defer(() -> {
            System.out.println("rollback: " + id);
            if(random.nextBoolean()){
                return Mono.empty();
            }

            return Mono.error(new RuntimeException("Conn error"));
        });
    }

    public Mono<Void> teste(){
        return Mono.defer(() -> {
            System.out.println("deu ruim: " + id);
            if(random.nextBoolean()){
                return Mono.empty();
            }

            return Mono.error(new RuntimeException("Conn error"));
        });
    }
}
