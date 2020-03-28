package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.util.UUID;

public class HotAndCold {

    // hot não precisa de alguem para disparar eventos, ele ja começa, no entanto se alguem se inscrever, vai receber apenas os novos envetos e não os ja gerados, antes da
    //sua inscrição.
    public static void main(String[] args) throws InterruptedException {
        //new HotAndCold().cold();
        new HotAndCold().connectableFlux();
    }

    public void cold(){ //somente dispara eventos, quando alguem se inscreva(uma requisição http por exemplo).
        Flux<String> coldPublisher = Flux.defer(() -> {
            System.out.println("Gerando novos elementos");
            return Flux.just(UUID.randomUUID().toString());
        });

        coldPublisher.subscribe(System.out::println);
    }

    //ConnectableFlux server para guarda os dados já processados por algum evento, assim, futuros assinantes que o desejam, pode pegar eles prontos, sem a necessidade de mandar
    //processar novamente.
    public void connectableFlux() throws InterruptedException {
        Flux<Integer> source = Flux.just(0,3)
                .doOnSubscribe(s -> System.out.println("Nova inscrição cold publisher"));

        ConnectableFlux<Integer> conn = source.publish();
        Thread.sleep(10000);
        conn.subscribe(e -> System.out.println("Subscriber1 onNext: " + e));
        Thread.sleep(1000);
        conn.subscribe(e -> System.out.println("Subscriber2 onNext: " + e));
        Thread.sleep(1000);
    }

}
