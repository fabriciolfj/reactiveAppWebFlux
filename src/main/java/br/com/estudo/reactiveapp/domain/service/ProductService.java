package br.com.estudo.reactiveapp.domain.service;

import br.com.estudo.reactiveapp.domain.model.Category;
import br.com.estudo.reactiveapp.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<Product> findAll();
    Flux<Product> findAllComNomeUpperCase();
    Flux<Product> findAllComNomeUpperCaseRepet();
    Mono<Product> findById(String id);
    Mono<Product> save(Product product);
    Mono<Void> delete(Product product);
    Flux<Category> findAllCategory();
    Mono<Category> findCategoryById(String id);
    Mono<Category> saveCategory(Category category);
    Mono<Category> findByNameCategory(String name);
    Mono<Product> findByNome(String nome);
}
