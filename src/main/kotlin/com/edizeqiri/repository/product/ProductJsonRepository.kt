package com.edizeqiri.repository.product

import com.edizeqiri.entity.Product
import com.edizeqiri.repository.LoanJsonRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty


@ApplicationScoped
class ProductJsonRepository(
    val mapper: ObjectMapper
) : LoanJsonRepository<Product>(), ProductRepository {

    @ConfigProperty(name = "repository.product")
    override lateinit var filePath: String

    override var data: List<Product> = mutableListOf()

    @PostConstruct
    fun init() {
        val json = load()
        data = mapper.readValue(json)
    }

    override fun findAllById(id: List<Long>): List<Product> {
        return data.filter { id.contains(it.id) }
    }

    override fun sum(id: List<Long>): Long {
        return data.filter { id.contains(it.id) }.sumOf { it.amount }
    }


}