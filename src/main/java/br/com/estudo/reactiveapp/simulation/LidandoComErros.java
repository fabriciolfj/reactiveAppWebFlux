package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

public class LidandoComErros {

    private Random random = new Random();

    //onErrorMap mapeia para uma nova exceção
    //onError interrompe o flux
    //retry tenta novamente indefinidamente ou por um período de tempo
    //retryBackoff  tenta novamente, com atrasos crescentes
    public static void main(String[] args) {
        new LidandoComErros().client();

        try{
            Thread.sleep(Integer.MAX_VALUE);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public void client(){
        Flux.just("User-1").flatMap(user -> recomendacaoLivros(user)
                .retryBackoff(5, Duration.ofMillis(100)) //vou tentar 5 vezes, com duração de 100 milisegundos
                .timeout(Duration.ofSeconds(3)) //se mão retornar após 3 segundos, mando um sinal de error
                .onErrorResume(e -> Flux.just("Alternativa"))) //onErrorResumo proporciona uma alternativa de trabalho em caso de erro.
                .subscribe(
                        b -> System.out.println("onNext: " + b),
                        e -> System.out.println("onError " + e.getMessage()),
                        () -> System.out.println("Complete")
                );
    }

    public Flux<String> recomendacaoLivros(String userId){
        return Flux.defer(() -> { //adiamos o calculo até que alguem assine o fluxo
            if(random.nextInt(10) < 7){
                return Flux.<String>error(new RuntimeException("Error"))
                        .delaySequence(Duration.ofMillis(100));
            }

           return Flux.just("Fabricio", "Jacob").delayElements(Duration.ofMillis(50));
        }).doOnSubscribe(s -> System.out.println("Requisição para " + userId)); // uso doOnSubscribe caso queira registrar alguma coisa, enquanto o fluxo não é consumido com sucesso.
    }
}
