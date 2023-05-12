package com.chaibin.batch.config.job

import com.chaibin.batch.company.entity.Employee
import com.chaibin.batch.company.repository.EmployeeRepository
import com.chaibin.batch.company.type.EmployeeStatusType
import com.chaibin.batch.config.SampleIncrementer
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class EmployeeJobConfig : DefaultBatchConfiguration() {

    // JpaPagingItemReader를 사용
    @Bean
    public fun employeeJobCase1(
        jobRepository: JobRepository,
        employeeFlowCase1: Step
    ): Job {
        return JobBuilder("employeeJobCase1", jobRepository)
            .incrementer(SampleIncrementer())
            .start(employeeFlowCase1)
            .build()
    }

    @Bean
    public fun employeeFlowCase1(
        jobRepository: JobRepository,
        platformTransactionManager: PlatformTransactionManager,
        employeeJpaPagingItemReader: ItemReader<Employee>,
        itemWriter: ItemWriter<Employee>
    ): Step {
        return StepBuilder("employeeStepCase1", jobRepository)
            .chunk<Employee, Employee>(1000, platformTransactionManager)
            .reader(employeeJpaPagingItemReader)
            .writer(itemWriter)
            .build()
    }

    @Bean
    public fun employeeJpaPagingItemReader(entityManagerFactory: EntityManagerFactory): ItemReader<Employee> {
        return JpaPagingItemReaderBuilder<Employee>()
            .name("employeeJpaPagingItemReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(10)
            .queryString("SELECT c FROM Employee c")
            .parameterValues(mapOf())
            .build()
    }

    @Bean
    public fun itemWriter(): ItemWriter<Employee> {
        return ItemWriter<Employee> {
            it.items.forEach { e -> e.changeApplied() }
        }
    }

    // RepositoryItemReader를 사용
    @Bean
    public fun employeeJobCase2(
        jobRepository: JobRepository,
        employeeFlowCase2: Step
    ): Job {
        return JobBuilder("employeeJobCase2", jobRepository)
            .start(employeeFlowCase2)
            .build()
    }

    @Bean
    public fun employeeFlowCase2(
        jobRepository: JobRepository,
        platformTransactionManager: PlatformTransactionManager,
        employeeRepositoryItemReader: ItemReader<Employee>,
        itemWriter: ItemWriter<Employee>
    ): Step {
        return StepBuilder("employeeStepCase1", jobRepository)
            .chunk<Employee, Employee>(1000, platformTransactionManager)
            .reader(employeeRepositoryItemReader)
            .writer(itemWriter)
            .build()
    }


    @Bean
    public fun employeeRepositoryItemReader(employeeRepository: EmployeeRepository): ItemReader<Employee> {
        return RepositoryItemReaderBuilder<Employee>()
            .name("employeeRepositoryItemReader")
            .repository(employeeRepository)
            .methodName("findEmployee")
            .sorts(mutableMapOf("id" to Sort.Direction.ASC))
            .pageSize(10)
            .build()
    }
}