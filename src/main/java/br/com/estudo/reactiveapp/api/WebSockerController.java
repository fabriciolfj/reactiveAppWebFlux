package br.com.estudo.reactiveapp.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.time.Duration;

@RequestMapping("/send")
@RestController
public class WebSockerController {

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void send(){
        WebSocketClient client = new ReactorNettyWebSocketClient();

        client.execute(
                URI.create("http://localhost:8080/ws/echo"),
                session -> Flux.interval(Duration.ofMillis(100))
                        .map(String::valueOf)
                        .map(session::textMessage)
                        .as(session::send) // o as trata o flux como mono
        );
    }
}
