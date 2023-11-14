package com.kumarp.departmentservice.controller;


import com.kumarp.departmentservice.dto.DepartmentRequest;
import com.kumarp.departmentservice.dto.DepartmentResponse;
import com.kumarp.departmentservice.model.Department;
import com.kumarp.departmentservice.service.DepartmentServiceUsingRestTemplate;
import com.kumarp.departmentservice.service.DepartmentServiceUsingWebClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@RequestMapping("/dept")
@RefreshScope
@Slf4j
public class DepartmentController {

     private DepartmentServiceUsingRestTemplate departmentService;
     // private DepartmentServiceUsingFeign departmentService;
     // private DepartmentServiceUsingWebClient departmentService;

    @Autowired
    private Environment environment;

    @GetMapping("/msg")
    public String getWelcomeMessage(){
        log.debug ( "getWelcomeMessage() of DepartmentController is Working!!" );
        return environment.getProperty ( "welcome.message" );
    }
    @GetMapping("/getMaxSalary")
    public Department getDeptWithMaxSalary() throws ExecutionException, InterruptedException {
        log.info ( "Here we are using the throws because of the CompletableFuture return type" );
        log.debug ( "getDeptWithMaxSalary() of DepartmentController is Working!!" );
        return departmentService.getDeptWithMaxSalary().get ();
    }

    @PostMapping("/addEmp")
    public DepartmentResponse saveDeptRecord(@RequestBody DepartmentRequest departmentRequest){
        log.debug ( "saveDeptRecord() of DepartmentController is Working!!" );
        return departmentService.saveDeptRecord( departmentRequest );
    }
    @GetMapping("/allEmp/{deptName}")
    public DepartmentResponse getAllEmployeeBasedOnDept(@PathVariable String deptName){
        log.debug ( "getAllEmployeeBasedOnDept() of DepartmentController is Working!!" );
        return departmentService.getAllEmployeeWithDeptName(deptName);
    }
}
