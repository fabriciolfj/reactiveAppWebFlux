package br.com.estudo.reactiveapp.domain.service;

import br.com.estudo.reactiveapp.domain.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RequestMonoFluxExemple {

    /**
     * fromCallable e uma espera futura, ou seja, executa o metodo de forma assincrona.
     */
    public Mono<User> requestUserData(Long id){ //adia até que alguem assine
        return Mono.defer(() ->
                isValidSession(id)
                        ? Mono.fromCallable(() -> requestUser(id)) : Mono.error(new RuntimeException("Id inválido")));
    }

    public Mono<User> requestUserDataDif(Long id){ //já valida
        return isValidSession(id) ? Mono.fromCallable(() -> requestUser(id)) : Mono.error(new RuntimeException("Id inválido"));
    }

    private User requestUser(Long id){
        return new User(id, "Teste");
    }

    private boolean isValidSession(Long id) {
        if(id % 2 == 0){
            return true;
        }

        return false;
    }
}
