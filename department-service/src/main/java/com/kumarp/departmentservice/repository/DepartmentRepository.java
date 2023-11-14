package com.kumarp.departmentservice.repository;

import com.kumarp.departmentservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    @Query("select dept from Department dept where dept.deptname = :newDeptName ")
    Optional<Department> findDeptIDByName(@Param ( "newDeptName" ) String newDeptName);
}
