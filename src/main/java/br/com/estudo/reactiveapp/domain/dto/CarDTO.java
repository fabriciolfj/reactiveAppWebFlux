package br.com.estudo.reactiveapp.domain.dto;

import br.com.estudo.reactiveapp.domain.model.enuns.FuelType;
import lombok.Data;

@Data
public class CarDTO {
    private int id;
    private String name;
    private FuelType fuelType;
}
