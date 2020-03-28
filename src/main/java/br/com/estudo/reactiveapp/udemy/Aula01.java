package br.com.estudo.reactiveapp.udemy;

import br.com.estudo.reactiveapp.udemy.domain.Comentarios;
import br.com.estudo.reactiveapp.udemy.domain.Usuario;
import br.com.estudo.reactiveapp.udemy.domain.UsuarioComentarios;
import java.time.Duration;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import lombok.extern.java.Log;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Aula01 {

    private static final Logger LOG = LoggerFactory.getLogger(Aula01.class);

    public static void main(String[] args) throws InterruptedException {
        new Aula01().controlandoContrapressaoManualmente();
    }

    private void controlandoContrapressaoManualmente(){
        Flux.range(1,10)
                .log()
                //.limitRate(5) // uma maneira
                .subscribe(new Subscriber<Integer>() {
                    private Subscription s;
                    private Integer limite = 5;
                    private Integer consumido = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(limite);
                    }

                    @Override
                    public void onNext(Integer t) {
//                        LOG.info(t.toString());
                        consumido++;
                        if(consumido == limite){
                            consumido = 0;
                            s.request(limite);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void exemploIntervaloInfinitoCreate() throws InterruptedException {
        Flux.create(emitter -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                private Integer contador = 0;

                @Override
                public void run() {

                    emitter.next(++contador); // posso mitir mais de um evento
                    if(contador == 10){
                        timer.cancel();
                        emitter.complete();
                    }

                    if(contador == 5){
                        timer.cancel();
                        emitter.error(new InterruptedException("Error, definido um flux de no máximo 5"));
                    }
                }
            }, 1000,1000);
        }).subscribe(next -> LOG.info(next.toString()), error -> LOG.error(error.getMessage()), () -> LOG.info("Finalizado"));
    }

    private void exemploIntervaloInfinito() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Flux.interval(Duration.ofSeconds(1))
                .doOnTerminate(latch::countDown)
                .flatMap(i -> {
                    if(i >= 5){
                        return Flux.error(new InterruptedException("Apenas 5 segundos."));
                    }
                    return Flux.just(i);
                }).map(i -> "Ola " + i)
        .retry(2)
        .subscribe(s -> LOG.info(s),e-> LOG.error(e.getMessage()));

        latch.await();
    }

    private void exemploDelayElementos(){
        Flux.range(12, 26)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(System.out::println)
        .subscribe();

        try {
            Thread.sleep(13000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void exemploComIntervalo(){
        Flux<Integer> numeros = Flux.range(12, 26);
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));

        numeros.zipWith(delay, (n, d) -> n)
                .doOnNext(System.out::println)
                .blockLast(); // nao usar em produção, pois o delay esta sendo executado em segundo plano, afim de exibir no console, usamos esse operador, //bloqueia até que o ultimo elemento seja emitido.

    }

    private void exemploZipWithRange(){
        Flux.just(1,2,3,4)
            .map(mapper -> mapper * 2)
            .zipWith(Flux.range(1,9), (um, dois) -> String.format("Primeiro Flux: %d, Segundo Flux: %d", um, dois))
            .subscribe(System.out::println);
    }

    private void exemploUsuarioComComentariosZipWithForma2(){ // uma forma de combinar fluxos com zipwith
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("Fabricio", "Jacob"));
        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentarios("Olá");
            comentarios.addComentarios("Bom dia");
            return comentarios;
        });

        usuarioMono.zipWith(comentariosUsuarioMono) // ele retonar uma tuple
                .map(mapper -> new UsuarioComentarios(mapper.getT1(), mapper.getT2()))
                .subscribe(user -> LOG.info(user.toString()));
    }


    private void exemploUsuarioComComentariosZipWith(){ // uma forma de combinar fluxos com zipwith
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("Fabricio", "Jacob"));
        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentarios("Olá");
            comentarios.addComentarios("Bom dia");
            return comentarios;
        });

        usuarioMono.zipWith(comentariosUsuarioMono, (u, comentariosUsuario) ->
                new UsuarioComentarios(u, comentariosUsuario) ).subscribe(user -> LOG.info(user.toString()));
    }


    private void exemploUsuarioComComentariosFlatMap(){ // uma forma de combinar fluxos com flatmap
        Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("Fabricio", "Jacob"));
        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentarios("Olá");
            comentarios.addComentarios("Bom dia");
            return comentarios;
        });

        usuarioMono.flatMap(u ->
                comentariosUsuarioMono.map(c -> new UsuarioComentarios(u,c))
        ).subscribe(user -> LOG.info(user.toString()));
    }

    private void exemploConverterListEmMono() {
        var dados = Arrays.asList(new Usuario("Fabricio" , "Jacob"), new Usuario("Bruce","Lee"), new Usuario("Sebastiao","Willis"));
        Flux.fromIterable(dados).map(usuario -> usuario.getNome().toUpperCase().concat(" ").concat(usuario.getSobreNome().toUpperCase()))
                .collectList()
                .subscribe(
                s -> {
                    s.forEach(item -> LOG.info(item));
                });
    }

    private void exemploToString() {
        var dados = Arrays.asList(new Usuario("Fabricio" , "Jacob"), new Usuario("Bruce","Lee"), new Usuario("Sebastiao","Willis"));
        Flux.fromIterable(dados).map(usuario -> usuario.getNome().toUpperCase().concat(" ").concat(usuario.getSobreNome().toUpperCase()))
                .flatMap(nome -> {
                    if(nome.contains("bruce".toUpperCase())){
                        return Mono.just(nome);
                    }

                    return Mono.empty();
                })
                .map(nome -> nome.toLowerCase()
                ).subscribe(
                s -> LOG.info(s.toString()));
    }

    private void exemploFlatMap() { //converte para outro fluxo observavel
        var dados = Arrays.asList("Fabricio Jacob", "Bruce Lee", "Sebastiao Willis");
        Flux.fromIterable(dados).map(nome -> new Usuario(nome.split(" ")[0].toUpperCase(), nome.split(" ").length > 1 ? nome.split(" ")[1].toUpperCase() : null))
                .flatMap(usuario -> {

                    if(usuario.getNome().equalsIgnoreCase("bruce")) {
                        return Mono.just(usuario);
                    }

                    return Mono.empty();
                })
                .map(usuario -> {
                    var nome = usuario.getNome().toLowerCase();
                    var sobreNome = usuario.getSobreNome().toLowerCase();
                    usuario.setNome(nome);
                    usuario.setSobreNome(sobreNome);
                    return usuario;
                }).subscribe(
                s -> LOG.info(s.toString()));
    }

    private void exemploIterable() {
        var dados = Arrays.asList("Carlos", "Suzana", "Teste", "Fabricio Jacob", "Bruce Lee", "Sebastiao Willis");
        Flux<String> nomes = Flux.fromIterable(dados);

        Flux<Usuario> usuarios = nomes.map(nome -> new Usuario(nome.split(" ")[0].toUpperCase(), nome.split(" ").length > 1 ? nome.split(" ")[1].toUpperCase() : null))
                .filter(usuario -> usuario.getSobreNome() != null)
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("Nome não pode ser vazio");
                    }

                    System.out.println(usuario);
                })
                .map(usuario -> {
                    var nome = usuario.getNome().toLowerCase();
                    var sobreNome = usuario.getSobreNome().toLowerCase();
                    usuario.setNome(nome);
                    usuario.setSobreNome(sobreNome);
                    return usuario;
                });

        usuarios.subscribe(
                s -> LOG.info(s.toString()),
                error -> LOG.error(error.getMessage()),
                () -> LOG.info("Complete")
        );
    }
}
