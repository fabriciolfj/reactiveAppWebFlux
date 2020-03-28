package br.com.estudo.reactiveapp.mapstruct;

import br.com.estudo.reactiveapp.domain.dto.DivisionDTO;
import br.com.estudo.reactiveapp.domain.dto.EmployeeDTO;
import br.com.estudo.reactiveapp.domain.model.*;
import br.com.estudo.reactiveapp.domain.service.CarsMapper;
import br.com.estudo.reactiveapp.domain.service.EmployeeMapper;
import br.com.estudo.reactiveapp.domain.service.SimpleSourceDestinationMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleSourceDestinationMapperIntegrationTest {

    private SimpleSourceDestinationMapper mapper = Mappers.getMapper(SimpleSourceDestinationMapper.class);
    private EmployeeMapper mapperNew = Mappers.getMapper(EmployeeMapper.class);
    private CarsMapper mapperCar = Mappers.getMapper(CarsMapper.class);
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    //@Test
    public void givenSourceToDestination_whenMaps_thenCorrect(){
        SimpleSource simpleSource = new SimpleSource();
        simpleSource.setName("Fabricio");
        simpleSource.setDescription("FabricioDescricao");
        SimpleDestination destination = mapper.sourceDestination(simpleSource);

        assertEquals(simpleSource.getName(), destination.getName());
        assertEquals(simpleSource.getDescription(), destination.getDescription());
    }

    //@Test
    public void givenEmployeeDTOwithDiffNametoEmployee_whenMaps_thenCorrect(){
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(1);
        dto.setEmployeeName("John");

        Employee entity = mapperNew.employeeDTOtoEmployee(dto);

        assertEquals(dto.getEmployeeId(), entity.getId());
        assertEquals(dto.getEmployeeName(), entity.getName());
    }

    //@Test
    public void givenEmpDTONestedMappingToEmp_whenMaps_thenCorrect() throws ParseException {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeStartDt("01-04-2016 01:00:00");
        var division = new DivisionDTO();
        division.setId(1);
        division.setName("Division1");
        dto.setDivision(division);

        Employee entity = mapperNew.employeeDTOtoEmployee(dto);
        assertEquals(dto.getDivision().getId(), entity.getDivision().getId());
        assertEquals(dto.getDivision().getName(), entity.getDivision().getName());

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        assertEquals(format.parse(dto.getEmployeeStartDt()).toString(), entity.getStartDt().toString());
    }

    //@Test
    public void givenEmpStartDtMappingToEmpDTO_whenMaps_thenCorrect() throws ParseException{
        Employee entity = new Employee();
        entity.setStartDt(new Date());
        EmployeeDTO dto = mapperNew.employeeToEmployeeDTO(entity);

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        assertEquals(format.parse(dto.getEmployeeStartDt()).toString(), entity.getStartDt().toString());
    }

    //@Test
    public void entityCarToCarDto(){
        Car car = new EletricCar();
        car.setId("1");
        car.setName("Ferrari");

        var dto = mapperCar.toCarDto(car);

        assertEquals(car.getId(), dto.getId());
        assertEquals(car.getName().toUpperCase(), dto.getName());
    }
}
