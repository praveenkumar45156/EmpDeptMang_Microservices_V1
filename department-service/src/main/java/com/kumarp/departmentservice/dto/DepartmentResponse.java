package com.kumarp.departmentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {
    private int deptid;
    private String deptname;
    private long totalsalary;
    private int noofemployee;
    private List<EmployeeResponse> employeeList;
}
