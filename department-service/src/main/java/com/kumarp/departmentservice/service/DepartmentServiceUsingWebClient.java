package com.kumarp.departmentservice.service;

import com.kumarp.departmentservice.WebClient.WebClientConfig;
import com.kumarp.departmentservice.dto.DepartmentRequest;
import com.kumarp.departmentservice.dto.DepartmentResponse;
import com.kumarp.departmentservice.dto.EmployeeResponse;
import com.kumarp.departmentservice.dto.MaxSalaryResponse;
import com.kumarp.departmentservice.model.Department;
import com.kumarp.departmentservice.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceUsingWebClient {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private final WebClient.Builder webClient;
    @Value( "${maxDeptURL}" )
    private String maxDeptURL;

    @Value ( "${addEmpURL}" )
    private String addEmpURL;

    @Value ( "${getAllEmpURL}" )
    private String getAllEmpUsingDeptNameURL;

    public DepartmentServiceUsingWebClient(WebClientConfig webClientConfig){
        this.webClient = webClientConfig.createWebClientObject ();
    }

    public Department getDeptWithMaxSalary() {
        MaxSalaryResponse employeeResponse = webClient.build ().get ().uri(maxDeptURL).retrieve ().bodyToMono ( MaxSalaryResponse.class ).block ();
        Optional<Department> departmentList = departmentRepository.findDeptIDByName (employeeResponse.getDeptName ());
        return departmentList.map ( value -> new Department (value.getDeptid (),value.getDeptname (),value.getTotalsalary (),
                value.getNoofemployee ())).orElse ( new Department ( 0,"",0L,0 ) );
    }

    public DepartmentResponse saveDeptRecord(DepartmentRequest departmentRequest) {
        System.out.println(departmentRequest.toString ());
        DepartmentResponse deptObj = new DepartmentResponse (  );
        Mono<ResponseEntity<String>> responseEntity = postEmployee(departmentRequest);
        if(responseEntity.block ().getStatusCode ().is2xxSuccessful ()){
            //System.out.println("Employee created successfully!");
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
            System.out.println("Failed to create the employee. Status code: " + responseEntity.block ().getStatusCode ());
        }

        return deptObj;
    }

    private Mono<ResponseEntity<String>> postEmployee(DepartmentRequest departmentRequest) {
        // Create HttpHeaders with the desired content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON);
        System.out.println(departmentRequest.toString ());
        List<EmployeeResponse> employeeResponseList = departmentRequest.getEmployeeList ();
        String apiUrl = addEmpURL;
        // Perform the POST request
        Mono<ResponseEntity<String>> responseEntity = webClient.build ().post ().uri ( apiUrl ).bodyValue ( employeeResponseList ).retrieve ().toEntity ( String.class );
        return responseEntity;
    }

    public DepartmentResponse getAllEmployeeWithDeptName(String deptName) {
        Optional<Department> deptObj = departmentRepository.findDeptIDByName ( deptName );
        DepartmentResponse departmentResponse = new DepartmentResponse ();
        // Call Another Service
        //String url = "http://employee-service/emp/allEmp/"+deptName;
        String url = getAllEmpUsingDeptNameURL+"/{deptName}";
        System.out.println(url);
        Mono<List<EmployeeResponse>> departObj = webClient.build ().get ().uri(url).retrieve ().bodyToMono ( new ParameterizedTypeReference<List<EmployeeResponse>>() {});

        departmentResponse.setDeptid ( deptObj.map(value -> value.getDeptid ()).orElse ( 0 ) );
        departmentResponse.setDeptname ( deptObj.map(value -> value.getDeptname ()).orElse ( "" )  );
        departmentResponse.setTotalsalary ( deptObj.map(value -> value.getTotalsalary ( )).orElse ( 0L )  );
        departmentResponse.setNoofemployee ( deptObj.map(value -> value.getNoofemployee ( )).orElse ( 0 ) );
        departmentResponse.setEmployeeList ( departObj.block () );

        return departmentResponse;
    }
}
