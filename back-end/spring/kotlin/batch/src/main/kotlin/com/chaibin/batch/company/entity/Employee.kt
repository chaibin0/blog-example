package com.chaibin.batch.company.entity

import com.chaibin.batch.company.type.EmployeeGenderType
import com.chaibin.batch.company.type.EmployeeStatusType
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
class Employee(
    id: Long?,
    name: String,
    status: EmployeeStatusType,
    gender: EmployeeGenderType,
    createdAt: LocalDateTime?
) {

    @Id
    @Column(name = "EMPLOYEE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = id
        private set

    @Column(name = "EMPLOYEE_NAME")
    var name: String = name
        private set

    @Column(name = "EMPLOYEE_STATUS")
    @Enumerated(EnumType.STRING)
    var status: EmployeeStatusType = status
        private set

    @Column(name = "EMPLOYEE_GENDER")
    @Enumerated(EnumType.STRING)
    var gender: EmployeeGenderType = gender
        private set

    @CreatedDate
    @Column(name = "CREATED_AT")
    var createdAt: LocalDateTime? = null
        private set

    fun changeApplied() {
        this.status = EmployeeStatusType.APPLIED
    }

    fun changeNotApplied() {
        this.status = EmployeeStatusType.NOT_APPLIED
    }
}