package br.com.estudo.reactiveapp.udemy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class UsuarioComentarios {

    private Usuario usuario;
    private Comentarios comentarios;
}
