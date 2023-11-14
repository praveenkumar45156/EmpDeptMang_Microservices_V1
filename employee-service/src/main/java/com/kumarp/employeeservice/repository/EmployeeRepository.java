package com.kumarp.employeeservice.repository;


import com.kumarp.employeeservice.model.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employees,Integer> {
    @Query(" From Employees emp where emp.deptname = :newDeptName")
    List<Employees> getAllEmpWithDept(@Param( "newDeptName" ) String newDeptName);

    @Query(value = "SELECT e.empid, e.deptname,e.dob, d.salary FROM employees e join (SELECT deptname,SUM(salary) as salary FROM employees GROUP BY deptname ORDER BY salary desc limit 1) d on d.deptname = e.deptname limit 1",nativeQuery = true)
    Employees getMaxDepartmentSalary();
}
