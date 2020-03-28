package br.com.estudo.reactiveapp.resource;

import br.com.estudo.reactiveapp.domain.model.User;
import br.com.estudo.reactiveapp.domain.service.RequestMonoFluxExemple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RequestExemploMonoFlux {

    @Autowired
    private RequestMonoFluxExemple request;

    @GetMapping("/adiar/{id}")
    public Mono<User> getUser(@PathVariable("id") Long id){
        return request.requestUserData(id);
    }

    @GetMapping("/executa/{id}")
    public Mono<User> getUserIsValid(@PathVariable("id") Long id){
        return request.requestUserDataDif(id);
    }

}
