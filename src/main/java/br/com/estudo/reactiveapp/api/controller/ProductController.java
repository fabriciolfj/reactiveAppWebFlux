package br.com.estudo.reactiveapp.api.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import br.com.estudo.reactiveapp.domain.model.Product;
import br.com.estudo.reactiveapp.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @Value("${config.uploads.path}")
    private String path;

    @GetMapping
    public Mono<ResponseEntity<Flux<Product>>> list(){
        return Mono.just(
                ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll())
        );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> findById(@PathVariable("id") String id){
        return productService.findById(id).map(p -> ResponseEntity.ok(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<HashMap<String, Object>>> create(@RequestBody @Valid Mono<Product> product){
        var request = new HashMap<String, Object>();
        return product.flatMap(prod -> {
            return productService.save(prod)
                    .map(p -> {
                        request.put("product", prod);
                        return ResponseEntity.created(URI.create("/api/product/".concat(p.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(request);
                    });
        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class)
                    .map(e -> e.getFieldErrors())
                    .flatMapMany(Flux::fromIterable)
                    .map(fieldError -> "O Campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collectList()
                    .flatMap(list -> {
                        request.put("errors", list);
                        return Mono.just(ResponseEntity.badRequest().body(request));
                    });
        });

    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> update(@PathVariable("id") String id, @RequestBody Product product){
        return productService.findById(id)
                .flatMap(p -> {
                    BeanUtils.copyProperties(product, p, "id");
                    return productService.save(p);
                })
                .map(prod -> ResponseEntity.created(URI.create("/api/product/".concat(prod.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                .body(prod))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> remove(@PathVariable("id") String id){
        return productService.findById(id)
                .flatMap(prod -> {
                    return productService.delete(prod).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/upload/{id}")
    public Mono<ResponseEntity<Product>> upload(@PathVariable String id, @RequestPart FilePart file){
        return productService.findById(id).flatMap(product -> {
            product.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                    .replace(" ", "")
                    .replace(":", "")
                    .replace("\\", "")

            );

            return file.transferTo(new File(path + product.getFoto())).then(productService.save(product));
        }).map( p -> ResponseEntity.ok(p)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/v2")
    public Mono<ResponseEntity<Product>> createOrFoto(Product product, @RequestPart FilePart file){
        if(product.getCreateAt() == null){
            product.setCreateAt(new Date());
        }

        product.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                .replace(" ", "")
                .replace(":", "")
                .replace("\\", "")

        );

        return file.transferTo(new File(path + product.getFoto())).then(productService.save(product))
                .map(prod -> ResponseEntity.created(URI.create("/api/product/".concat(prod.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(prod))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
