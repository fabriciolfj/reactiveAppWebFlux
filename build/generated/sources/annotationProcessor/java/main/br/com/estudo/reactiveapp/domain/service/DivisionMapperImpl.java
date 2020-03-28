package br.com.estudo.reactiveapp.domain.service;

import br.com.estudo.reactiveapp.domain.dto.DivisionDTO;
import br.com.estudo.reactiveapp.domain.model.Division;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-26T23:15:32-0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Ubuntu)"
)
@Component
public class DivisionMapperImpl implements DivisionMapper {

    @Override
    public DivisionDTO divisionToDivisionDTO(Division entity) {
        if ( entity == null ) {
            return null;
        }

        DivisionDTO divisionDTO = new DivisionDTO();

        divisionDTO.setId( entity.getId() );
        divisionDTO.setName( entity.getName() );

        return divisionDTO;
    }

    @Override
    public Division divisionDTOtoDivision(DivisionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Division division = new Division();

        division.setId( dto.getId() );
        division.setName( dto.getName() );

        return division;
    }
}
