package br.com.estudo.reactiveapp.simulation;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class SubscriberSimulation {

    public static void main(String[] args) throws InterruptedException {
        //subscritpionNormal();
        //subscriptionControlandoAssinatura();
        //cancelandoUmStream();
        customSubscribe();
    }

    private static void customSubscribe(){
        Subscriber<String> subscriber = new Subscriber<String>() { //não é recomendada dessa maneira

            volatile Subscription subscription;
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                System.out.println("Requisição inicial para 1 elemento.");
                subscription.request(1);
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext "+ s);
                System.out.println("Requisitando mais 1 elemento");
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        Flux<String> stream = Flux.just("Hello", "world", "!");
        /*stream.subscribe(subscriber);*/

        var meuSubscriber = new MeuSubscriber<String>();//recomendado
        stream.subscribe(meuSubscriber);
    }

    private static void cancelandoUmStream() throws InterruptedException {//posso usar o disposable para interromper o consumo de um fluxo (stream)
        var disposable = Flux.interval(Duration.ofMillis(5)).subscribe(
                data -> System.out.println("onNext: " + data)
        );

        Thread.sleep(200);

        disposable.dispose();
    }

    private static  void subscriptionControlandoAssinatura(){
        Flux.range(1,100).subscribe(
                data -> System.out.println("onNext: " + data),
                err -> {},
                () -> System.out.println("Complete"),
                subscription -> {
                    subscription.request(4); //vou querer 4 primeiros elementos
                    subscription.cancel(); //cancela para nao consumir os demais
                }
        );
    }

    private static void subscritpionNormal() {
        Flux.just(1,2,3,4).subscribe(
                data -> System.out.println("onNext: " + data),
                        err -> {
                            System.out.println("Error");
                        },
        () -> System.out.println("Complete"));
    }

    static class MeuSubscriber<T> extends BaseSubscriber<T>{
        @Override
        protected void hookOnSubscribe(Subscription subscription) {
            System.out.println("Requisição Inicial para o 1 element.");
            request(1);
        }

        @Override
        protected void hookOnNext(T value) {
            System.out.println("onNext: " + value);
            System.out.println("Requisitando mais 1 elemento");
            request(1);
        }
    }
}
