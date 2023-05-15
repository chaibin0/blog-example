package com.chaibin.batch.config.flow

import com.chaibin.batch.company.entity.Employee
import jakarta.transaction.Transactional
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class EmployeeStatusItemWriter() : ItemWriter<Employee> {

    @Transactional
    override fun write(chunk: Chunk<out Employee>) {
        for (item in chunk.items) {
            item.changeNotApplied()
        }
    }
}