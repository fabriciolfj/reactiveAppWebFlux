package br.com.estudo.reactiveapp.domain.service;

import br.com.estudo.reactiveapp.domain.dto.CarDTO;
import br.com.estudo.reactiveapp.domain.model.Car;
import br.com.estudo.reactiveapp.domain.model.EletricCar;
import br.com.estudo.reactiveapp.domain.model.enuns.FuelType;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public abstract class CarsMapper {

    @BeforeMapping
    protected void convertDtoComBaseNoFuelType(Car car, @MappingTarget CarDTO carDTO){
        if(car instanceof EletricCar){
            carDTO.setFuelType(FuelType.ELECTRIC);
            return;
        }

        carDTO.setFuelType(FuelType.BIO_DIESEL);
    }

    @AfterMapping
    protected void convertNameToUpperCase(@MappingTarget CarDTO carDTO){
        carDTO.setName(carDTO.getName().toUpperCase());
    }

    public abstract CarDTO toCarDto(Car car);

}
