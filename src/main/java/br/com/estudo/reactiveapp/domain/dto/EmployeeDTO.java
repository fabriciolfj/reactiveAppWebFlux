package br.com.estudo.reactiveapp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class EmployeeDTO {
    private int employeeId;
    private String employeeName;
    private DivisionDTO division;
    private String employeeStartDt;
}
