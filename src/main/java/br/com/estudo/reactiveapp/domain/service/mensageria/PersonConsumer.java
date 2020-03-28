package br.com.estudo.reactiveapp.domain.service.mensageria;

import br.com.estudo.reactiveapp.domain.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonConsumer {

    @Autowired
    private ObjectMapper mapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "q.person", durable = "true"),
            exchange = @Exchange(value = "q.person")
     //       key = ""
    ))
    public void consumer(Person person){
        System.out.println(person);
        /*try{
            System.out.println(json);
            var person = mapper.readValue(json, Person.class);
            System.out.println(person);
        }catch(Exception e){
            e.printStackTrace();
        }*/
    }
}
