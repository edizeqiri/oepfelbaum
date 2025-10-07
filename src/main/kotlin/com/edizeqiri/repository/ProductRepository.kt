package com.edizeqiri.repository

import com.edizeqiri.entity.Product
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class ProductRepository(
    val mapper: ObjectMapper
) : LoanRepository<Product>() {

    @ConfigProperty(name = "repository.product")
    override lateinit var file: String

    override var data: List<Product> = mutableListOf()

    @PostConstruct
    fun init() {
        val json = load()
        data = mapper.readValue(json)
    }


    fun findAllById(id: List<Long>): List<Product> {
        return data.filter { id.contains(it.id) }
    }

    fun sum(id: List<Long>): Long {
        return data.filter { id.contains(it.id) }.sumOf { it.amount }
    }


}