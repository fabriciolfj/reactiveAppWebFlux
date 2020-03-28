package br.com.estudo.reactiveapp.domain;
import java.util.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatService<T> {
    Flux<T> getMessagesStream();
    Mono<List<T>> getMessagesAfter(String messageId);

}
