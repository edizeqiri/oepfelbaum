package com.edizeqiri.repository.limit

import com.edizeqiri.entity.Limit
import com.edizeqiri.repository.LoanJsonRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class LimitJsonRepository(
    val mapper: ObjectMapper
) : LoanJsonRepository<Limit>(), LimitRepository {

    @ConfigProperty(name = "repository.limit")
    override lateinit var filePath: String

    override var data: List<Limit> = mutableListOf()

    @PostConstruct
    fun init() {

        val json = load()
        data = mapper.readValue(json)

    }

    override fun findById(id: Long): Limit {
        return data.first { id == it.id }
    }
}