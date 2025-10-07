package com.edizeqiri.repository.financing

import com.edizeqiri.entity.FinancingObject
import com.edizeqiri.repository.LoanJsonRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class FinancingObjectJsonRepository(
    val mapper: ObjectMapper
) : LoanJsonRepository<FinancingObject>(), FinancingObjectRepository {

    @ConfigProperty(name = "repository.financing_object")
    override lateinit var filePath: String

    override var data: List<FinancingObject> = mutableListOf()

    @PostConstruct
    fun init() {
        val json = load()
        data = mapper.readValue(json)
    }

    override fun findAllByUserId(userId: String): FinancingObject {
        return data.first { it.owners.first().id.toString() == userId }
    }

}