package com.edizeqiri.repository

import com.edizeqiri.entity.FinancingObject
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class FinancingObjectRepository(
    val mapper: ObjectMapper
) : LoanRepository<FinancingObject>() {

    @ConfigProperty(name = "repository.financing_object")
    override lateinit var file: String

    override var data: List<FinancingObject> = mutableListOf()

    @PostConstruct
    fun init() {
        val json = load()
        data = mapper.readValue(json)
    }

    fun findAllByUserId(userId: String): List<FinancingObject> {
        return data.filter { it.owners.first().id.toString() == userId }
    }

}