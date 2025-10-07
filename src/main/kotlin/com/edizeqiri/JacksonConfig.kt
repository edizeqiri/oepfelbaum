package com.edizeqiri

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ApplicationScoped
class JacksonConfig {

    @Produces
    fun objectMapper(): ObjectMapper {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val javaTimeModule = JavaTimeModule().apply {
            addDeserializer(LocalDate::class.java, LocalDateDeserializer(formatter))
        }

        return ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .registerModule(javaTimeModule)
    }
}
