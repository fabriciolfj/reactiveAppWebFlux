package br.com.estudo.reactiveapp.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document(collection = "products")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    private static final long serialVersionUID = 7438491129276949087L;

    @Id
    @EqualsAndHashCode.Include
    private String id;
    @NotBlank
    private String nome;
    @NotNull
    private Double preco;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonIgnore
    private Date createAt;
    @Valid
    @NotNull
    private Category category;
    private String foto;

    public Product(String nome, Double preco){
        this.nome = nome;
        this.preco = preco;
    }

    public Product(String nome, Double preco, Category category){
        this(nome, preco);
        this.category = category;
    }

}
