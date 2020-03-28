package br.com.estudo.reactiveapp.domain.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import br.com.estudo.reactiveapp.domain.model.Category;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    Mono<Category> findByNome(String nome);
}
