package com.kumarp.employeeservice.controller;


import com.kumarp.employeeservice.dto.EmployeeResponseDTO;
import com.kumarp.employeeservice.dto.MaxSalaryResponse;
import com.kumarp.employeeservice.model.Employees;
import com.kumarp.employeeservice.services.EmployeeServiceUsingRestTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/emp")
@Slf4j
public class EmployeeController {

    private EmployeeServiceUsingRestTemplate employeeServiceUsingRestTemplate;
    @GetMapping("/allEmp/{deptName}")
    public List<Employees> getAllEmp(@PathVariable String deptName){
        log.debug ( "getAllEmp() of employeeController is working !!" );
        return employeeServiceUsingRestTemplate.getAllEmp(deptName);
    }

    @GetMapping("/maxDept")
    public MaxSalaryResponse getMaxDepartment(){
        log.debug ( "getMaxDepartment() of employeeController is working !!" );
        return employeeServiceUsingRestTemplate.getMaxDepartment();
    }

    @PostMapping("/addEmp")
    public ResponseEntity<List<Employees>> addEmp(@RequestBody List<EmployeeResponseDTO> employeeResponseDTOList){
        log.debug ( "addEmp() of employeeController is working !!" );
        return employeeServiceUsingRestTemplate.addEmp(employeeResponseDTOList);
    }
}
