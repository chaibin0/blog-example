package com.chaibin.batch.company.repository

import com.chaibin.batch.company.entity.Employee
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface EmployeeRepository : JpaRepository<Employee, Long> {

    @Query(value = "Select c FROM Employee c")
    fun findAllEmployee(pageable: Pageable): Slice<Employee>

}