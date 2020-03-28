package br.com.estudo.reactiveapp.simulation;

import br.com.estudo.reactiveapp.domain.model.Car;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/*
publisOn recebe sinais do servidor para o client (downstream) e repete os mesmo para o client ao servidor (upstream), enquanto executa o retorno da chamada em um worker (trabalhador) do
scheduler associado.
Consequentemente, afeta onde os operadores subseqüentes serão executados (até que outro publishOn seja encadeado).

subscribeOn se aplica ao processo de assinatura, quando essa cadeia reversa é construída. Como conseqüência, não importa onde você coloque o subscribeOn na cadeia, ele sempre afeta o contexto da emissão de origem. No entanto, isso não afeta o comportamento de chamadas subseqüentes para publishOn. Eles ainda alternam o contexto de execução para a parte da cadeia após eles.

publishOn força o próximo operador (e possivelmente os operadores subsequentes após o próximo) a executar em um encadeamento diferente. Da mesma forma, subscribeOn força o operador anterior (e possivelmente operadores anteriores ao anterior) a executar em um encadeamento diferente.
 */

public class Parallel {

    public static void main(String[] args) {
        //new Parallel().test().subscribe(System.out::println);
        new Parallel().test2();
    }

    public Mono<String> test(){
        return Mono.fromCallable(() -> getCar())
                .subscribeOn(scheduler()) // limito o numero de threads
                .publishOn(Schedulers.elastic()) // tipo de gerenciamento
                .map(this::convert)
                .log("processando entidade carro");
    }

    public void test2(){ //delego para o reactor controlar as threads
        Mono.just(getCar())
                //.publishOn(Schedulers.elastic()) // tipo de gerenciamento
                //.doOnNext(t -> System.out.println("Recebido " + t + ", thread: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.parallel())
                .subscribe(System.out::println);
                //.log("processando entidade carro");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void imprimir(Car carro) {
        System.out.println(carro);
    }

    private Mono<String> testeNew(String car) {
        return Mono.just(car);
    }

    private String convert(Car car) {
        return "Car: " + car.getName();
    }

    public Car getCar(){
        return new Car("001", "Fusca");
    }

    public Scheduler scheduler() {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(15));
    }
}
