package com.kumarp.departmentservice.service;

import com.kumarp.departmentservice.Feign.EmployeeServiceClient;
import com.kumarp.departmentservice.dto.DepartmentRequest;
import com.kumarp.departmentservice.dto.DepartmentResponse;
import com.kumarp.departmentservice.dto.EmployeeResponse;
import com.kumarp.departmentservice.dto.MaxSalaryResponse;
import com.kumarp.departmentservice.model.Department;
import com.kumarp.departmentservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceUsingFeign {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeServiceClient employeeServiceClient;

    public Department getDeptWithMaxSalary() {
        MaxSalaryResponse maxSalaryResponse = employeeServiceClient.getMaxDepartment ();
        Optional<Department> departmentList = departmentRepository.findDeptIDByName (maxSalaryResponse.getDeptName ());
        return departmentList.map ( value -> new Department (value.getDeptid (),value.getDeptname (),
                value.getTotalsalary (),value.getNoofemployee ())).orElse ( new Department ( 0,"",0L,0 ) );
    }

    public DepartmentResponse saveDeptRecord(DepartmentRequest departmentRequest) {
        System.out.println(departmentRequest.toString ());
        DepartmentResponse deptObj = new DepartmentResponse (  );
        ResponseEntity<List<EmployeeResponse>> responseEntity = postEmployee(departmentRequest);
        if(responseEntity.getStatusCode ().is2xxSuccessful ()){
            System.out.println("Employee created successfully!");
            departmentRepository.save(new Department (
                    departmentRequest.getDeptid (),
                    departmentRequest.getDeptname ( ),
                    departmentRequest.getTotalsalary ( ),
                    departmentRequest.getNoofemployee ( ) ));

            deptObj.setDeptid ( departmentRequest.getDeptid ( ) );
            deptObj.setDeptname ( departmentRequest.getDeptname ( ) );
            deptObj.setTotalsalary ( departmentRequest.getTotalsalary ( ) );
            deptObj.setNoofemployee ( departmentRequest.getNoofemployee ( ) );
            deptObj.setEmployeeList ( departmentRequest.getEmployeeList () );

        }else{
            System.out.println("Failed to create the employee. Status code: " + responseEntity.getStatusCode());
        }

        return deptObj;
    }
    private ResponseEntity<List<EmployeeResponse>> postEmployee(DepartmentRequest departmentRequest) {

        // Create HttpHeaders with the desired content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON);
        System.out.println(departmentRequest.toString ());
        List<EmployeeResponse> employeeResponseList = departmentRequest.getEmployeeList ();
        ResponseEntity<List<EmployeeResponse>> responseEntity = employeeServiceClient.addEmp ( employeeResponseList );

        return responseEntity;
    }

    public DepartmentResponse getAllEmployeeWithDeptName(String deptName) {
        Optional<Department> deptObj = departmentRepository.findDeptIDByName ( deptName );
        DepartmentResponse departmentResponse = new DepartmentResponse ();
        // Call Another Service
        List<EmployeeResponse> departObj = employeeServiceClient.getAllEmp ( deptName );
        departmentResponse.setDeptid ( deptObj.map(value -> value.getDeptid ()).orElse ( 0 ) );
        departmentResponse.setDeptname ( deptObj.map(value -> value.getDeptname ()).orElse ( "" )  );
        departmentResponse.setTotalsalary ( deptObj.map(value -> value.getTotalsalary ( )).orElse ( 0L )  );
        departmentResponse.setNoofemployee ( deptObj.map(value -> value.getNoofemployee ( )).orElse ( 0 ) );
        departmentResponse.setEmployeeList ( departObj );

        return departmentResponse;
    }
}
