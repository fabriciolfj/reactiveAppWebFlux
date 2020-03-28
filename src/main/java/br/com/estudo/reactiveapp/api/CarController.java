package br.com.estudo.reactiveapp.api;

import br.com.estudo.reactiveapp.domain.model.Car;
import br.com.estudo.reactiveapp.domain.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/carro")
public class CarController {

    private WebClient webClient;

    public CarController(WebClient.Builder webClient){
        this.webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public Mono save(){
         return webClient.post()
                .uri("/carros")
                .body(BodyInserters.fromPublisher(Mono.just(new Car("1", "Fusca Novo")), Car.class))
                .exchange() // ideal caso queria consumir um cookie ou header, caso não queira, pode usar o retrieve,
                .flatMap( response -> {
                    if(response.statusCode().is2xxSuccessful()){
                        System.out.println("ok");
                        return Mono.empty();
                    }

                    return Mono.error(new RuntimeException("Falha ao salvar o carro"));
                });
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Car> get(){
        return webClient.get()
                .uri("/carros")
                .retrieve()
                .bodyToFlux(Car.class);
    }
}
