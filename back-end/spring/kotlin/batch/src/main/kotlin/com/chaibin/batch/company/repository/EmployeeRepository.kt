package com.chaibin.batch.company.repository

import com.chaibin.batch.company.entity.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface EmployeeRepository : JpaRepository<Employee, Long> {

    @Query(value = "Select c FROM Employee c WHERE c.gender = com.chaibin.batch.company.type.EmployeeStatusType.NOT_APPLIED")
    public fun findEmployee(): List<Employee>

}