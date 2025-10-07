package com.edizeqiri.repository

import com.edizeqiri.entity.Limit
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class LimitRepository(
    val mapper: ObjectMapper
) : LoanRepository<Limit>() {

    @ConfigProperty(name = "repository.limit")
    override lateinit var file: String

    override var data: List<Limit> = mutableListOf()

    @PostConstruct
    fun init() {

        val json = load()
        data = mapper.readValue(json)

    }

    fun findById(id: Long): List<Limit> {
        return data.filter { id == it.id }
    }
}