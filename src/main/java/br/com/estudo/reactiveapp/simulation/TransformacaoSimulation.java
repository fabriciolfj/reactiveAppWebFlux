package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.util.function.Tuple2;

import java.util.function.Function;

public class TransformacaoSimulation {

    public static void main(String[] args) {
        //transform();
        compose();//atualiza o comportamento a cada novo assinante
    }

    private static void compose(){
        Function<Flux<String>, Flux<String>> logUserInf = stream -> stream.index()
                .doOnNext(s -> System.out.println(s.getT1() + " user: " + s.getT2()))
                .map(Tuple2::getT2);

        Flux.just(5,4)
                .map(i -> "user teste " + i)
                .compose(logUserInf) //reaproveitando a função passando para ele cada evento desse novo produtor. e ele atualiza o comportamento apenas uma vez, na fase de montagem
                .subscribe(s -> System.out.println(s));
    }

    private static void transform() {
        Hooks.onOperatorDebug();//para debugar fluxo reativos.
        Function<Flux<String>, Flux<String>> logUserInf = stream -> stream.index()
                .doOnNext(s -> System.out.println(s.getT1() + " user: " + s.getT2()))
                .map(Tuple2::getT2);

        Flux.just(5,4)
                .map(i -> "user teste " + i)
                .transform(logUserInf) //reaproveitando a função passando para ele cada evento desse novo produtor. e ele atualiza o comportamento apenas uma vez, na fase de montagem
                .subscribe(s -> System.out.println(s));
    }
}
