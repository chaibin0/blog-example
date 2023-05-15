package com.chaibin.batch.config.datasource

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = ["com.chaibin.batch.company.repository"],
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager"
)
@EnableTransactionManagement
@EnableConfigurationProperties(value = [CompanyDatasourceProperties::class])
class CompanyDataSourceConfig(val companyDatasourceProperties: CompanyDatasourceProperties) {

    @Primary
    @Bean("dataSource")
    fun companyDataSource(): DataSource {
        return HikariDataSource().apply {
            jdbcUrl = companyDatasourceProperties.url
            username = companyDatasourceProperties.username
            password = companyDatasourceProperties.password
            driverClassName = companyDatasourceProperties.driverClassName
            maximumPoolSize = companyDatasourceProperties.maximumPoolSize
            maxLifetime = companyDatasourceProperties.maxLifeTime
            poolName = companyDatasourceProperties.poolName
        }
    }

    @Bean
    @Primary
    fun entityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        return builder.dataSource(companyDataSource())
            .packages("com.chaibin.batch.company.entity")
            .persistenceUnit("companyPersistenceUnit")
            .build()
    }

    @Bean
    @Primary
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        val txManager = JpaTransactionManager()
        txManager.entityManagerFactory = entityManagerFactory
        return txManager
    }
}