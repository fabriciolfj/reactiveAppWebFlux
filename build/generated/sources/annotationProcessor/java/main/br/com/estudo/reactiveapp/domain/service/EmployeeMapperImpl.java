package br.com.estudo.reactiveapp.domain.service;

import br.com.estudo.reactiveapp.domain.dto.DivisionDTO;
import br.com.estudo.reactiveapp.domain.dto.EmployeeDTO;
import br.com.estudo.reactiveapp.domain.model.Division;
import br.com.estudo.reactiveapp.domain.model.Employee;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-26T23:15:32-0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Ubuntu)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDTO employeeToEmployeeDTO(Employee entity) {
        if ( entity == null ) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setEmployeeName( entity.getName() );
        employeeDTO.setEmployeeId( entity.getId() );
        if ( entity.getStartDt() != null ) {
            employeeDTO.setEmployeeStartDt( new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" ).format( entity.getStartDt() ) );
        }
        employeeDTO.setDivision( divisionToDivisionDTO( entity.getDivision() ) );

        return employeeDTO;
    }

    @Override
    public Employee employeeDTOtoEmployee(EmployeeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setName( dto.getEmployeeName() );
        try {
            if ( dto.getEmployeeStartDt() != null ) {
                employee.setStartDt( new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" ).parse( dto.getEmployeeStartDt() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        employee.setId( dto.getEmployeeId() );
        employee.setDivision( divisionDTOToDivision( dto.getDivision() ) );

        return employee;
    }

    protected DivisionDTO divisionToDivisionDTO(Division division) {
        if ( division == null ) {
            return null;
        }

        DivisionDTO divisionDTO = new DivisionDTO();

        divisionDTO.setId( division.getId() );
        divisionDTO.setName( division.getName() );

        return divisionDTO;
    }

    protected Division divisionDTOToDivision(DivisionDTO divisionDTO) {
        if ( divisionDTO == null ) {
            return null;
        }

        Division division = new Division();

        division.setId( divisionDTO.getId() );
        division.setName( divisionDTO.getName() );

        return division;
    }
}
