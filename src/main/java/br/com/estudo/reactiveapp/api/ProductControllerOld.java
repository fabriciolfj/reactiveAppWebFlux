package br.com.estudo.reactiveapp.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import br.com.estudo.reactiveapp.domain.model.Category;
import br.com.estudo.reactiveapp.domain.model.Product;
import br.com.estudo.reactiveapp.domain.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class ProductControllerOld {

    public static final String REDIRECT_LISTAR_ERROR_NAO_EXISTE_O_PRODUTO = "redirect:/listar?error=nao+existe+o+produto";
    @Value("${config.uploads.path}")
    private String path;

    @Autowired
    private ProductService productService;
    private static final Logger log = LoggerFactory.getLogger(ProductControllerOld.class);

    @GetMapping("/uploads/img/{nomeFoto:.+}")
    public Mono<ResponseEntity<Resource>> verFoto(@PathVariable String nomeFoto) throws MalformedURLException {
        Path rota = Paths.get(path).resolve(nomeFoto).toAbsolutePath();
        Resource imagem = new UrlResource(rota.toUri());

        return Mono.just(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imagem.getFilename() + "\"")
                .body(imagem)
        );
    }

    @ModelAttribute("categorias")
    public Flux<Category> categorias() {
        return productService.findAllCategory();
    }

    @GetMapping("/ver/{id}")
    public Mono<String> ver(Model model, @PathVariable String id){
        return productService.findById(id)
                .doOnNext(p -> {
                    model.addAttribute("product", p);
                    model.addAttribute("titulo", "Detalhe product");
                }).switchIfEmpty(Mono.just(new Product()))
                .flatMap(p -> {
                    if(p.getId() == null){
                        return Mono.error(new InterruptedException("Não existe o produto."));
                    }
                    return Mono.just(p);
                }).then(Mono.just("ver"))
                .onErrorResume(ex -> Mono.just(REDIRECT_LISTAR_ERROR_NAO_EXISTE_O_PRODUTO));
    }

    @GetMapping({"/listar", "/"})
    public String list(Model model) {
        Flux<Product> products = productService.findAllComNomeUpperCase();

        products.subscribe(System.out::println);

        model.addAttribute("products", products);
        model.addAttribute("titulo", "Lista de produtos");
        return "listar";
    }

    @GetMapping("/listar-datadriver") //contrapressão
    public String listDataDriver(Model model) {
        Flux<Product> products = productService.findAllComNomeUpperCase()
                .delayElements(Duration.ofSeconds(1));

        products.subscribe(System.out::println);

        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 1));
        model.addAttribute("titulo", "Lista de produtos");
        return "listar";
    }

    @GetMapping("/listar-full")
    public String listFull(Model model) {
        Flux<Product> products = productService.findAllComNomeUpperCaseRepet();

        model.addAttribute("products", products);
        model.addAttribute("titulo", "Lista de produtos");
        return "listar";
    }

    @GetMapping("/listar-chunked")
    public String listchunked(Model model) {
        Flux<Product> products = productService.findAllComNomeUpperCaseRepet();

        model.addAttribute("products", products);
        model.addAttribute("titulo", "Lista de produtos");
        return "listar-chunked";
    }

    @GetMapping("/form")
    public Mono<String> criar(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("titulo", "Formulário de produto");
        model.addAttribute("button", "Criar");
        return Mono.just("form");
    }

    @PostMapping("/form")
    public Mono<String> save(@Valid @ModelAttribute(name = "product") Product product, BindingResult result, Model model, @RequestPart(name = "file") FilePart file) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Error no formulário produto.");
            model.addAttribute("button", "Guardar");
            return Mono.just("/form");
        }

        return productService.findCategoryById(product.getCategory().getId())
                .flatMap(c -> {
                    if (product.getCreateAt() == null) {
                        product.setCreateAt(new Date());
                    }

                    if(!file.filename().isEmpty()){
                        product.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                                .replace(" ", "")
                                .replace(":", "")
                                .replace("\\", "")

                        );
                    }

                    product.setCategory(c);
                    return productService.save(product);
                }).doOnNext(p -> {
                    log.info("Categoria vinculada: " + p.getCategory().getNome());
                    log.info("Produto salvo: " + p.getNome() + " id: " + p.getId());
                }).flatMap(p -> {
                    if(!file.filename().isEmpty()){
                        return file.transferTo(new File(path + p.getFoto()));
                    }
                    return Mono.empty();
                })
                .thenReturn("redirect:/listar?success=produto+salvo+com+sucesso");
    }

    @GetMapping("/form/{id}")
    public Mono<String> editar(@PathVariable String id, Model model, BindingResult result) {
        var productMono = productService.findById(id).doOnNext(p -> {
            log.info("Product: " + p.getNome());
        }).defaultIfEmpty(new Product());

        model.addAttribute("titulo", "Editar Produto");
        model.addAttribute("product", productMono);

        return Mono.just("/form");
    }

    @GetMapping("/form-v2/{id}")
    public Mono<String> editarV2(@PathVariable String id, Model model) {
        return productService.findById(id).doOnNext(p -> {
            log.info("Product: " + p.getNome());
            model.addAttribute("titulo", "Editar Produto");
            model.addAttribute("product", p);
            model.addAttribute("button", "editar");
        }).defaultIfEmpty(new Product())
                .flatMap(p -> {
                    if (p.getId() == null) {
                        return Mono.error(new InterruptedException("Não existe o produto"));
                    }
                    return Mono.just(p);
                })
                .thenReturn("/form")
                .onErrorResume(ex -> Mono.just("redirect:/listar?error=nao+existe+o+produto"));
    }

    @GetMapping("/eliminar/{id}")
    public Mono<String> eliminar(@PathVariable String id) {
        return productService.findById(id)
                .defaultIfEmpty(new Product())
                .flatMap(p -> {
                    if (p.getId() == null) {
                        return Mono.error(new InterruptedException("Produto inexistente para exclusão."));
                    }

                    return Mono.just(p);
                })
                .flatMap(productService::delete).then(Mono.just("redirect:/listar?success=produto+excluido+com+sucesso"))
                .onErrorResume(ex -> Mono.just("redirect:/listar?error=não+existe+produto+para+excluir"));
    }


}
