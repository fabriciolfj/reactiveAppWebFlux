package br.com.estudo.reactiveapp.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.estudo.reactiveapp.domain.dao.ProductRepository;
import br.com.estudo.reactiveapp.domain.model.Product;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductRepository dao;
    private static final Logger LOG = LoggerFactory.getLogger(ProductRestController.class);

    @GetMapping
    public Flux<Product> index(){
        Flux<Product> products = dao.findAll().map(product -> {
            product.setNome(product.getNome().toUpperCase());
            return product;
        }).doOnNext(prod -> LOG.info(prod.getNome()));

        return products;
    }

    @GetMapping("/{id}")
    public Mono<Product> show(@PathVariable String id){
        return dao.findAll()
                .filter(p -> p.getId().equals(id)).log().next();
    }

}
