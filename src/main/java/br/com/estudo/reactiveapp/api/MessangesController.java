package br.com.estudo.reactiveapp.api;

import br.com.estudo.reactiveapp.domain.MessageResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mensagens")
public class MessangesController {

    @GetMapping
    public List<MessageResponse> get(){
        return lista();
    }

    private List<MessageResponse> lista() {
        var data = new ArrayList<MessageResponse>();
        for(int i = 0; i <  1000; i++){
            data.add(new MessageResponse("Fabricio", "teste " + i));
        }

        return data;
    }
}
