package br.com.estudo.reactiveapp.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "categorias")
public class Category implements Serializable {


    private static final long serialVersionUID = -8458640360314486571L;

    public Category(String id){
        this.id = id;
    }

    @Id
    @EqualsAndHashCode.Include
    @NotBlank
    private String id;
    private String nome;
}
