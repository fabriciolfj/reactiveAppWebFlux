package br.com.estudo.reactiveapp.domain.service;

import br.com.estudo.reactiveapp.domain.ChatService;
import br.com.estudo.reactiveapp.domain.MessageResponse;
import org.springframework.amqp.core.Message;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.List;

@Service
public class GitterService implements ChatService {

    private final WebClient webClient;

    public GitterService(){
        this.webClient = WebClient.builder().build();
    }

    @Override
    public Flux<MessageResponse> getMessagesStream() {
        return webClient.get().uri(URI.create("http://localhost:9090/mensagens"))
                .retrieve()
                .bodyToFlux(MessageResponse.class)
                .retryBackoff(Long.MAX_VALUE, Duration.ofMillis(500)); // tentar novamente a cada 500 milisegundos.
    }

    @Override
    public Mono<List<MessageResponse>> getMessagesAfter(String messageId) {
        return webClient.get().uri(URI.create("http://localhost:9090/mensagens"))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MessageResponse>>() {
                }).timeout(Duration.ofSeconds(1));
    }
}
