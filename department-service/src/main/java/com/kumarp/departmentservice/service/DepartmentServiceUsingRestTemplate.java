package com.kumarp.departmentservice.service;


import com.kumarp.departmentservice.RestTemplate.RestTemplateConfig;
import com.kumarp.departmentservice.dto.DepartmentRequest;
import com.kumarp.departmentservice.dto.DepartmentResponse;
import com.kumarp.departmentservice.dto.EmployeeResponse;
import com.kumarp.departmentservice.dto.MaxSalaryResponse;
import com.kumarp.departmentservice.model.Department;
import com.kumarp.departmentservice.repository.DepartmentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class DepartmentServiceUsingRestTemplate {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired // this will work when you will create the bean in the Application main class because the component scan is present there
    private RestTemplate restTemplate;
    //Note: to enable the @Value please activate the profile else face error
    @Value ( "${maxDeptURL}" )
    private String maxDeptURL;

    @Value ( "${addEmpURL}" )
    private String addEmpURL;

    @Value ( "${getAllEmpURL}" )
    private String getAllEmpUsingDeptNameURL;

//        public DepartmentServiceUsingRestTemplate(RestTemplateConfig restTemplateConfig){
//        this.restTemplate = restTemplateConfig.createRestTemplateObject ();
//    }

//    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceUsingRestTemplate.class);
    @CircuitBreaker ( name = "deptWithMaxSalary", fallbackMethod = "fallBackMethod")
    @TimeLimiter ( name = "deptWithMaxSalary" )
    @Retry ( name = "deptWithMaxSalary" )
    public CompletableFuture<Department> getDeptWithMaxSalary() {
//        String maxDeptURL = "http://localhost:8082/emp/maxDept";
//        try{
//            Thread.sleep ( 10000 );
//        }catch (Exception e){
//            System.out.println (e.getMessage () );
//        }
        log.debug ( "getDeptWithMaxSalary() of DepartmentServiceUsingRestTemplate is Working!!" );
        MaxSalaryResponse employeeResponse = restTemplate.getForObject ( maxDeptURL, MaxSalaryResponse.class);
        Optional<Department> departmentList = departmentRepository.findDeptIDByName (employeeResponse.getDeptName ());
        return CompletableFuture.supplyAsync ( () -> departmentList.map ( value -> new Department (value.getDeptid (),value.getDeptname (),value.getTotalsalary (),
                value.getNoofemployee ())).orElse ( new Department ( 0,"",0L,0 ) ) ) ;
    }
    public CompletableFuture<Department> fallBackMethod(Exception e){
        Department nullDepartmentObj = new Department ( 0,"",0L,0 );
        log.debug ( "fallBackMethod() of DepartmentServiceUsingRestTemplate is Working!!" );
        log.info ( "Oops!...Something Went Wrong Maximum Salary is not available" );
        log.info (e.getMessage ());
        return CompletableFuture.supplyAsync ( () ->  nullDepartmentObj);
    }

    public DepartmentResponse saveDeptRecord(DepartmentRequest departmentRequest) {
        log.debug ( "saveDeptRecord() of DepartmentServiceUsingRestTemplate is Working!!" );
        DepartmentResponse deptObj = new DepartmentResponse (  );
        ResponseEntity<String> responseEntity = postEmployee(departmentRequest);
        if(responseEntity.getStatusCode ().is2xxSuccessful ()){
            log.info("Employee created successfully!");
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
            log.info ( "Failed to create the employee. Status code: " + responseEntity.getStatusCode() );
        }
        return deptObj;
    }

    private ResponseEntity<String> postEmployee(DepartmentRequest departmentRequest) {
        log.debug ( "postEmployee() of DepartmentServiceUsingRestTemplate is Working!!" );
        log.info("Create HttpHeaders with the desired content type");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON);
        System.out.println(departmentRequest.toString ());
        List<EmployeeResponse> employeeResponseList = departmentRequest.getEmployeeList ();
        log.info("Create an HttpEntity with the product data and headers");
        HttpEntity<List<EmployeeResponse>> requestEntity = new HttpEntity<> (employeeResponseList, headers);
        // String apiUrl = "http://employee-service/emp/addEmp";
        String apiUrl = addEmpURL;
        log.info("Perform the POST request");
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        return responseEntity;
    }

    public DepartmentResponse getAllEmployeeWithDeptName(String deptName) {
        log.debug ( "getAllEmployeeWithDeptName() of DepartmentServiceUsingRestTemplate is Working!!" );
        Optional<Department> deptObj = departmentRepository.findDeptIDByName ( deptName );
        DepartmentResponse departmentResponse = new DepartmentResponse ();
        log.info("Call Another Service");
        // String apiUrl = "http://localhost:8082/emp/allEmp/{deptName}"; // Replace with your API URL
        String apiUrl = getAllEmpUsingDeptNameURL+"/{deptName}";
        System.out.println(apiUrl);
        log.info("Send an HTTP GET request and deserialize the response into a List of MyObject");
        List<EmployeeResponse> departObj = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EmployeeResponse>> () {},
                deptName
        ).getBody();
        departmentResponse.setDeptid ( deptObj.map(value -> value.getDeptid ()).orElse ( 0 ) );
        departmentResponse.setDeptname ( deptObj.map(value -> value.getDeptname ()).orElse ( "" )  );
        departmentResponse.setTotalsalary ( deptObj.map(value -> value.getTotalsalary ( )).orElse ( 0L )  );
        departmentResponse.setNoofemployee ( deptObj.map(value -> value.getNoofemployee ( )).orElse ( 0 ) );
        departmentResponse.setEmployeeList ( departObj );
        return departmentResponse;
    }
}
