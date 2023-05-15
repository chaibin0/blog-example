package com.chaibin.batch.config.job

import com.chaibin.batch.company.entity.Employee
import com.chaibin.batch.config.SampleIncrementer
import com.chaibin.batch.config.flow.EmployeeStatusItemWriter
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


// JpaPagingItemReader를 사용

@Configuration
@ConditionalOnProperty(prefix = "spring.batch.job", name = ["name"], havingValue = "employeeTestJob1")
class EmployeeJobConfig : DefaultBatchConfiguration(){
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
            .chunk<Employee, Employee>(CHUNK_SIZE, transactionManager)
            .reader(employeeJpaPagingItemReader)
            .writer(itemWriter)
            .build()
    }

    @Bean
    fun employeeJpaPagingItemReader(entityManagerFactory: EntityManagerFactory): ItemReader<Employee> {
        return JpaPagingItemReaderBuilder<Employee>()
            .name("employeeJpaPagingItemReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT c FROM Employee c")
            .parameterValues(mapOf())
            .build()
    }

    companion object {
        const val CHUNK_SIZE = 9
    }
}