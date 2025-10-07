package com.edizeqiri

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import java.time.OffsetDateTime

@ApplicationScoped
class JacksonConfig {

    @Produces
    fun objectMapper(): ObjectMapper {
        val javaTimeModule = JavaTimeModule().apply {
            addSerializer(OffsetDateTime::class.java, OffsetDateTimeSerializer.INSTANCE)
        }

        return ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .registerModule(javaTimeModule)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
}