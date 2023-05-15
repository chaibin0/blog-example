package com.chaibin.batch.config.job

import com.chaibin.batch.company.entity.Employee
import com.chaibin.batch.config.SampleIncrementer
import com.chaibin.batch.config.flow.EmployeeStatusItemWriter
import jakarta.persistence.EntityManagerFactory
import jakarta.transaction.Transactional
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.transaction.PlatformTransactionManager

// JpaPagingItemReader를 사용

@Configuration
class EmployeeJobConfig {

    @Bean
    fun employeeTestJob1(
        jobRepository: JobRepository,
        employeeFlowCase1: Step
    ): Job {
        return JobBuilder("employeeTestJob1", jobRepository)
            .preventRestart()
            .incrementer(SampleIncrementer())
            .start(employeeFlowCase1)
            .build()
    }

    @Bean
    fun employeeFlowCase1(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        employeeJpaPagingItemReader: ItemReader<Employee>,
        itemWriter: EmployeeStatusItemWriter
    ): Step {
        return StepBuilder("employeeStepCase1", jobRepository)
            .chunk<Employee, Employee>(10, transactionManager)
            .reader(employeeJpaPagingItemReader)
            .writer(itemWriter)
            .build()
    }

    @Bean
    fun employeeJpaPagingItemReader(entityManagerFactory: EntityManagerFactory): ItemReader<Employee> {
        return JpaPagingItemReaderBuilder<Employee>()
            .name("employeeJpaPagingItemReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(9)
            .queryString("SELECT c FROM Employee c")
            .parameterValues(mapOf())
            .build()
    }
}