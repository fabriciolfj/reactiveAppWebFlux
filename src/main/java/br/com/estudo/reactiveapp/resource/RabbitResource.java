package br.com.estudo.reactiveapp.resource;

import br.com.estudo.reactiveapp.domain.model.Person;
import br.com.estudo.reactiveapp.domain.service.mensageria.PersonProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class RabbitResource {

    @Autowired
    private PersonProducer personProducer;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void test(){
        var person = new Person("Fabricio", 34);
        personProducer.sendMessage(person);
    }
}
