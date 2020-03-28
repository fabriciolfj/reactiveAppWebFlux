package br.com.estudo.reactiveapp.domain.rotas;

import br.com.estudo.reactiveapp.domain.model.Car;
import br.com.estudo.reactiveapp.domain.rotas.handlers.CarHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

//@Configuration
public class RoutesCar {

    //@Bean
    public RouterFunction<ServerResponse> routes(CarHandler handler){
        return nest(path("/carros"),
                nest(accept(APPLICATION_JSON),route(GET("/{id}"), handler::get))
                .andRoute(method(HttpMethod.POST), handler::create)
                .andRoute(method(HttpMethod.GET), handler::findAll));
    }
}
