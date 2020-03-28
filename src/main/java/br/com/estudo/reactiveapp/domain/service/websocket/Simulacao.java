package br.com.estudo.reactiveapp.domain.service.websocket;

import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.time.Duration;

public class Simulacao {

    public static void main(String[] args) throws InterruptedException {
        new Simulacao().enviar();
    }

    public void enviar() throws InterruptedException {
        WebSocketClient client = new ReactorNettyWebSocketClient();

        client.execute(
                URI.create("http://localhost:8080/ws/echo"),
                session -> Flux.interval(Duration.ofMillis(100))
                .map(String::valueOf)
                .map(session::textMessage)
                .as(session::send) // o as trata o flux como mono
         );

        Thread.sleep(10000);
    }
}
