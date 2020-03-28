package br.com.estudo.reactiveapp.domain.service;

import br.com.estudo.reactiveapp.domain.dto.DivisionDTO;
import br.com.estudo.reactiveapp.domain.model.Division;
import org.mapstruct.Mapper;

@Mapper
public interface DivisionMapper {
    DivisionDTO divisionToDivisionDTO(Division entity);
    Division divisionDTOtoDivision(DivisionDTO dto);
}
