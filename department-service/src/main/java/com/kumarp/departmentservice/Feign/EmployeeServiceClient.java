package com.kumarp.departmentservice.Feign;

import com.kumarp.departmentservice.dto.MaxSalaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.kumarp.departmentservice.dto.EmployeeResponse;
import java.util.List;

@FeignClient(name = "employee-service", url = "http://localhost:8082")
public interface EmployeeServiceClient {
    /*
    getDeptWithMaxSalary = http://localhost:8082/emp/maxDept
    saveDeptRecord = http://localhost:8082/emp/addEmp
    getEmpNumber = http://localhost:8082/emp/allEmp/{deptName}
     */
    @GetMapping("/emp/allEmp/{deptName}")
    public List<EmployeeResponse> getAllEmp(@PathVariable String deptName);
    @GetMapping("/emp/maxDept")
    public MaxSalaryResponse getMaxDepartment();
    @PostMapping("/emp/addEmp")
    public ResponseEntity<List<EmployeeResponse>> addEmp(@RequestBody List<EmployeeResponse> employeeResponseDTOList);
}
