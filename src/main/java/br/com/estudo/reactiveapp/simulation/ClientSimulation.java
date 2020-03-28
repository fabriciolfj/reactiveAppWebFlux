package br.com.estudo.reactiveapp.simulation;

import br.com.estudo.reactiveapp.domain.model.Car;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

public class ClientSimulation {

    final WebClient webClient;

    public ClientSimulation(){
        this.webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
    }

    public static void main(String[] args) {
        new ClientSimulation().post();
    }

    public void post(){
        webClient.post()
                .uri("/carros")
                .body(BodyInserters.fromPublisher(Mono.just(new Car("1", "Fusca novo")), Car.class))
                .exchange() // ideal caso queria consumir um cookie ou header, caso não queira, pode usar o retrieve,
                .flatMap( response -> {
                    if(response.statusCode().is2xxSuccessful()){
                        System.out.println("ok");
                        return Mono.empty();
                    }

                    return Mono.error(new RuntimeException("Falha ao salvar o carro"));
                });
    }
}
