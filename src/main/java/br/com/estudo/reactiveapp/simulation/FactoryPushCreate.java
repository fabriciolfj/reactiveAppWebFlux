package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.stream.IntStream;

public class FactoryPushCreate {

    //criar programaticamente um fluxo de dados
    public static void main(String[] args) {
        new FactoryPushCreate().generate();
    }

    //funciona de forma similar ao push, mas podemos enviar em diferentes eventos
    public void create(){
        Flux.create(emitter -> {
            emitter.onDispose(() -> System.out.println("Disposed")); //dispose chama doOnCancel
        }).subscribe(System.out::println);

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }


    //push gera um fluxsink (fluxo quente)
    public void push(){//ideal para adaptar uma api async em uma unica thread (ou adaptar uma api para o contexto reativo), sem se preocupar com backpressure e cancelamento
        Flux.push(emitter -> IntStream.range(2000,3000)
            .forEach(emitter::next))
                .delayElements(Duration.ofMillis(1))
                .subscribe(e -> System.out.println("onNext: " + e));

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // criar sequencias mais complexas com base em um estado interno encaminhado ao gerador, ele requer um valor inicial e uma função
    //exige um estado intermediario entre as emissões.
    public void generate(){
        Flux.generate(
                () -> Tuples.of(0L, 1L), (state, sink) -> { //valor inicial a uma função lambda
            System.out.println("Gerando o valor: " + state.getT2());
            sink.next(state.getT2()); //chamando o proximo que será propagado
            long newValue = state.getT1() + state.getT2();
            return Tuples.of(state.getT2(), newValue); //recalcula um novo par de estado com base no próximo valor da sequencia
        }).delayElements(Duration.ofMillis(1)) //latência para simular
                .take(7) //pegamos os 7 primeiros elementos
                .subscribe(e -> System.out.println("onNext: " + e)); //assinando o evento

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
