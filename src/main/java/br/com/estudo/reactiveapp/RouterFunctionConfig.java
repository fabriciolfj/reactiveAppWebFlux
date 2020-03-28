package br.com.estudo.reactiveapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.estudo.reactiveapp.domain.rotas.handlers.ProductHandler;
import lombok.RequiredArgsConstructor;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler handler){
        return route(GET("/api/v2/products"), handler::get)
                .andRoute(GET("/api/v2/products/{id}"), handler::findById)
                .andRoute(POST("/api/v2/products"), handler::create)
                .andRoute(PUT("/api/v2/products/{id}"), handler::update)
                .andRoute(DELETE("/api/v2/products/{id}"), handler::delete)
                .andRoute(POST("/api/v2/products/upload/{id}"), handler::upload)
                .andRoute(POST("/api/v2/products/createPhoto"), handler::createProductPhoto);
    }
}
