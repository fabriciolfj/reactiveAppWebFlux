package br.com.estudo.reactiveapp.simulation;

import reactor.core.publisher.Flux;

public class UsandoRecursosDescartaveisConnectionSimulation {

    public static void main(String[] args){
        //normal();
        //reativo();
        new UsandoRecursosDescartaveisConnectionSimulation().usingWhen();
    }

    public void usingWhen(){ //gerenciar recursos de forma reativa, recupera o recurso de forma sincronizada e reativa
        Flux.usingWhen(Transaction.beginTransaction(),
                transaction -> transaction.insertRows(Flux.just("A", "B", "C")), Transaction::commit, Transaction::teste) // fluxo dos dados, se der certo, se deu ruim0
                .subscribe(
                        d -> System.out.println("onNext " + d),
                        e -> System.out.println("onError " + e.getMessage()),
                        () -> System.out.println("onComplete")
                );

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void reativo(){ //using
        Flux<String> ioRequestResults = Flux.using(Connection::newConnection,//criando o recurso
                conn -> Flux.fromIterable(conn.getData()), // convertendo em um fluxo de dados
                Connection::close); // limpando o recurso após o uso

        ioRequestResults.subscribe(data -> System.out.println("Dados recebidos: " + data),
                e -> System.out.println("Error: " + e.getMessage()),
                () -> System.out.println("Finalizado"));
    }

    private static void normal() {
        try(Connection conn = Connection.newConnection()){
            conn.getData().forEach(data -> System.out.println("Recebido os dados: " + data));
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
