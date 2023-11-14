package com.kumarp.employeeservice.services;

import com.kumarp.employeeservice.dto.EmployeeResponseDTO;
import com.kumarp.employeeservice.dto.MaxSalaryResponse;
import com.kumarp.employeeservice.model.Employees;
import com.kumarp.employeeservice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeServiceUsingRestTemplate {

    private EmployeeRepository employeeRepository;
    public List<Employees> getAllEmp(String deptName) {
        log.debug ( "getAllEmp() of EmployeeServiceUsingRestTemplate is working !!" );
        List<Employees> employeesList = employeeRepository.getAllEmpWithDept(deptName);
        return employeesList;
    }

    public MaxSalaryResponse getMaxDepartment() {
        log.debug ( "getMaxDepartment() of EmployeeServiceUsingRestTemplate is working !!" );
        Employees employees = employeeRepository.getMaxDepartmentSalary();
        MaxSalaryResponse maxSalaryResponse = new MaxSalaryResponse ();
        maxSalaryResponse.setDeptName ( employees.getDeptname () );
        maxSalaryResponse.setSalary ( employees.getSalary () );
        return maxSalaryResponse;
    }

    public ResponseEntity<List<Employees>> addEmp(List<EmployeeResponseDTO> employeeResponseDTOList) {
        log.debug ( "addEmp() of EmployeeServiceUsingRestTemplate is working !!" );
        List<Employees> employeesList = employeeRepository.saveAll ( employeeResponseDTOList.stream ().map(value -> new Employees ( value.getEmpid (),value.getDeptname (),value.getDob (),value.getSalary () )).toList () );
        return ResponseEntity.ok ( employeesList );
    }
}
