package com.chaibin.batch.config

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.JobParametersIncrementer

class SampleIncrementer : JobParametersIncrementer {
    override fun getNext(parameters: JobParameters?): JobParameters {

        val id = parameters?.getLong("run.id", 1L)?.plus(1L) ?: 1L
        return JobParametersBuilder().addLong("run.id", id).toJobParameters()
    }
}