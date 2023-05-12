package com.chaibin.batch.config.datasource

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "datasource.company")
data class CompanyDatasourceProperties(
    val url: String,
    val username: String,
    val password: String,
    val driverClassName: String,
    val maximumPoolSize: Int,
    val maxLifeTime: Long,
    val poolName: String
)