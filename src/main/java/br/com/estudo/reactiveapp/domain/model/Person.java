package br.com.estudo.reactiveapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Person implements Serializable {

    private static final long serialVersionUID = -4248580589761565236L;
    private String nome;
    private int age;

    public Person(){

    }
}
