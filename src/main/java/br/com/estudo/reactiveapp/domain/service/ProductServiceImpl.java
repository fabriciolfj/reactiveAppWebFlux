package br.com.estudo.reactiveapp.domain.service;


import org.springframework.stereotype.Service;

import java.util.Date;

import br.com.estudo.reactiveapp.domain.dao.CategoryRepository;
import br.com.estudo.reactiveapp.domain.dao.ProductRepository;
import br.com.estudo.reactiveapp.domain.model.Category;
import br.com.estudo.reactiveapp.domain.model.Product;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Flux<Product> findAllComNomeUpperCase() {
        return productRepository.findAll().map(p -> {
            p.setNome(p.getNome().toUpperCase());
            return p;
        });
    }



    @Override
    public Flux<Product> findAllComNomeUpperCaseRepet() {
        return findAllComNomeUpperCase().repeat(5000);
    }

    @Override
    public Mono<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        if(product.getCreateAt() == null){
            product.setCreateAt(new Date());
        }

        return productRepository.save(product);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return productRepository.delete(product);
    }

    @Override
    public Flux<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Mono<Category> findCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Mono<Category> saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Mono<Category> findByNameCategory(String name) {
        return categoryRepository.findByNome(name);
    }

    @Override
    public Mono<Product> findByNome(String nome) {
        return productRepository.findByNome(nome);
    }
}
