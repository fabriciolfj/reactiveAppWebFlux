package br.com.estudo.reactiveapp.domain.rotas.handlers;

import br.com.estudo.reactiveapp.domain.model.Car;
import br.com.estudo.reactiveapp.domain.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyExtractors.toFlux;
import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

//@Component
public class CarHandler {

    //@Autowired
    private CarService carService;

    public Mono<ServerResponse> get(ServerRequest request){
        Mono<Car> carro = Mono.just(carService.get(request.pathVariable("id")));
        return ok().contentType(APPLICATION_JSON).body(carro, Car.class);
    }

    public Mono<ServerResponse> create(ServerRequest request){
        return request.bodyToMono(Car.class)
                .flatMap(carService::save).flatMap(o -> ServerResponse.created(URI.create("/carros/" + o.getId())).build());
        /*return request.body(toMono(Car.class)).doOnNext(carService::save)
                .flatMap(o -> ServerResponse.created(URI.create("/carros/" + o.getId()))
                        .contentType(APPLICATION_JSON).build());*/
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromProducer(Mono.just(carService.findAll()), Car.class))
                .switchIfEmpty(notFound().build());
    }

}
