package br.com.estudo.reactiveapp.domain.rotas.handlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerRequestExtensionsKt;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

import br.com.estudo.reactiveapp.domain.model.Category;
import br.com.estudo.reactiveapp.domain.model.Product;
import br.com.estudo.reactiveapp.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductService productService;
    private final Validator validator;

    @Value("${config.uploads.path}")
    private String path;

    public Mono<ServerResponse> createProductPhoto(ServerRequest request){
        Mono<Product> product = request.multipartData().map(multipart -> {
            FormFieldPart nome = (FormFieldPart) multipart.toSingleValueMap().get("nome");
            FormFieldPart preco = (FormFieldPart) multipart.toSingleValueMap().get("preco");
            FormFieldPart categoriaId = (FormFieldPart) multipart.toSingleValueMap().get("categoria.id");
            FormFieldPart categoriaNome = (FormFieldPart) multipart.toSingleValueMap().get("categoria.nome");

            Category category = new Category(categoriaId.value(), categoriaNome.value());
            return new Product(nome.value(), Double.parseDouble(preco.value()), category);
        });

        return request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(file -> product.flatMap(p -> {
                    p.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                            .replace(" ", "-")
                            .replace(":", "")
                            .replace("\\", ""));
                    return file.transferTo(new File(path + p.getFoto())).then(productService.save(p));
                })).flatMap(p -> ServerResponse.created(URI.create("/api/v2/products/".concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON).body(fromValue(p)));
    }

    public Mono<ServerResponse> upload(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(file -> productService.findById(id).flatMap(p -> {
                    p.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                            .replace(" ", "-")
                            .replace(":", "")
                            .replace("\\", ""));
                    return file.transferTo(new File(path + p.getFoto())).then(productService.save(p));
                })).flatMap(p -> ServerResponse.created(URI.create("/api/v2/products/".concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON).body(fromValue(p)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll(), Product.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return productService.findById(id).flatMap(p ->
                ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(p)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Product> product = request.bodyToMono(Product.class);

        return product.flatMap(p -> {

            Errors errors = new BeanPropertyBindingResult(p, Product.class.getName());
            validator.validate(p, errors);

            if(errors.hasErrors()){
                return Flux.fromIterable(errors.getFieldErrors())
                        .map(fieldError -> "Campo " + fieldError.getField() + " - " + fieldError.getDefaultMessage())
                        .collectList()
                        .flatMap(list -> ServerResponse.badRequest().body(fromValue(list)));
            }

            if (p.getCreateAt() == null) {
                p.setCreateAt(new Date());
            }

            return productService.save(p)
                    .flatMap(pbd -> ServerResponse.created(URI.create("/api/v2/products/".concat(pbd.getId())))
                            .contentType(MediaType.APPLICATION_JSON).body(fromValue(pbd)));
        });
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<Product> product = request.bodyToMono(Product.class);
        String id = request.pathVariable("id");

        Mono<Product> productDb = productService.findById(id);

        return productDb.zipWith(product, (db, req) -> {
            db.setNome(req.getNome());
            db.setCategory(req.getCategory());
            db.setFoto(req.getFoto());
            db.setPreco(req.getPreco());
            return db;
        }).flatMap(p -> ServerResponse.created(URI.create("/api/v2/products/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON).body(productService.save(p), Product.class))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Product> product = productService.findById(id);
        return product.flatMap(p -> productService.delete(p)).then(ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
