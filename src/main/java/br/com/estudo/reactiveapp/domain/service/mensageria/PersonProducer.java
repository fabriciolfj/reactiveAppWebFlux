package br.com.estudo.reactiveapp.domain.service.mensageria;

import br.com.estudo.reactiveapp.domain.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(Person p){
        /*try{
            var json = objectMapper.writeValueAsString(p);
            rabbitTemplate.convertAndSend("q.person",json);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }*/
        rabbitTemplate.convertAndSend("q.person",p);
    }
}
