package br.com.estudo.reactiveapp.udemy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Usuario {

    private String nome;
    private String sobreNome;
}
