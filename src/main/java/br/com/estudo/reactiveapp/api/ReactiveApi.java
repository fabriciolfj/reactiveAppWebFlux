package br.com.estudo.reactiveapp.api;

import br.com.estudo.reactiveapp.domain.ChatService;
import br.com.estudo.reactiveapp.domain.MessageResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.util.function.Function;

@RequestMapping("/v1/api")
@RestController
public class ReactiveApi {

    final ReplayProcessor<MessageResponse> messagesStream = ReplayProcessor.create(50); // pega as ultimas 50 mensagens e manda as mesmas e as novas

    public ReactiveApi(ChatService<MessageResponse> chatService){
        Flux.mergeSequential(chatService.getMessagesAfter(null)
                .flatMapIterable(Function.identity()), chatService.getMessagesStream())
                .subscribe(messagesStream);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MessageResponse> stream(){
        return messagesStream;
    }

}
