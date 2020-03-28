package br.com.estudo.reactiveapp.udemy.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comentarios {

    private List<String> comentarios = new ArrayList<>();

    public void addComentarios(String comentario){
        this.comentarios.add(comentario);
    }
}
