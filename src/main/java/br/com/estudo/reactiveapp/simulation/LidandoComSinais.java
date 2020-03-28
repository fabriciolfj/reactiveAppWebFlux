package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;

public class LidandoComSinais {

    public static void main(String[] args) {
        new LidandoComSinais().materializeDesmaterialize();
    }

    //quase queira fazer algum durante o processo da pipeline
    public void doOnEach(){// lida com todos os sinais onError, onNext, onComplete e onSubscribe
        Flux.just(1,2,3).concatWith(Flux.error(new RuntimeException("Error")))
                .doOnEach(System.out::println)
                .subscribe();
    }

    //convertendo fluxo de dados para fluxo de sinais e vice versa
    public void materializeDesmaterialize(){
        Flux.range(1,3)
                .doOnNext(e -> System.out.println("data " + e))
                .materialize()
                .doOnNext(e -> System.out.println("signal " + e))
                .dematerialize()
                .collectList()
                .subscribe(e -> System.out.println("result " + e));
    }
}
