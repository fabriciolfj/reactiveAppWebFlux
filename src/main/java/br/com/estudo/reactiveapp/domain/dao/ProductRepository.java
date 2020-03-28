package br.com.estudo.reactiveapp.domain.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import br.com.estudo.reactiveapp.domain.model.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    Mono<Product> findByNome(String nome);
}
