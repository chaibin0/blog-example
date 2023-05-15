package com.chaibin.batch.config.job

import com.chaibin.batch.company.entity.Employee
import com.chaibin.batch.company.repository.EmployeeRepository
import com.chaibin.batch.config.SampleIncrementer
import com.chaibin.batch.config.flow.EmployeeStatusItemWriter
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.transaction.PlatformTransactionManager


// RepositoryItemReader를 사용
@Configuration
class EmployeeJob2Config {

    @Bean
    fun employeeTestJob2(
        jobRepository: JobRepository,
        employeeFlowCase2: Step
    ): Job {
        return JobBuilder("employeeTestJob2", jobRepository)
            .preventRestart()
            .incrementer(SampleIncrementer())
            .start(employeeFlowCase2)
            .build()
    }

    @Bean
    fun employeeFlowCase2(
        jobRepository: JobRepository,
        platformTransactionManager: PlatformTransactionManager,
        employeeRepositoryItemReader: ItemReader<Employee>,
        itemWriter: EmployeeStatusItemWriter
    ): Step {
        return StepBuilder("employeeStepCase2", jobRepository)
            .chunk<Employee, Employee>(9, platformTransactionManager)
            .reader(employeeRepositoryItemReader)
            .writer(itemWriter)
            .build()
    }

    @Bean
    fun employeeRepositoryItemReader(employeeRepository: EmployeeRepository): ItemReader<Employee> {
        return RepositoryItemReaderBuilder<Employee>()
            .name("employeeRepositoryItemReader")
            .repository(employeeRepository)
            .methodName("findAllEmployee")
            .sorts(mutableMapOf("id" to Sort.Direction.ASC))
            .pageSize(9)
            .build()
    }
}