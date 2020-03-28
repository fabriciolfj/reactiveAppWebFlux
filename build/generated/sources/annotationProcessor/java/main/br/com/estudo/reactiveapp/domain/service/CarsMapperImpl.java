package br.com.estudo.reactiveapp.domain.service;

import br.com.estudo.reactiveapp.domain.dto.CarDTO;
import br.com.estudo.reactiveapp.domain.model.Car;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-26T23:15:32-0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Ubuntu)"
)
@Component
public class CarsMapperImpl extends CarsMapper {

    @Override
    public CarDTO toCarDto(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDTO carDTO = new CarDTO();

        convertDtoComBaseNoFuelType( car, carDTO );

        if ( car.getId() != null ) {
            carDTO.setId( Integer.parseInt( car.getId() ) );
        }
        carDTO.setName( car.getName() );

        convertNameToUpperCase( carDTO );

        return carDTO;
    }
}
