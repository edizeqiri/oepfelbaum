package com.edizeqiri.entity

data class FinancingObject(

    val id: Long,
    val owner: List<Owner>,
    val limit: Long,
    val products: List<Long>,
    val status: Status
)

data class Owner(
    val id: Long,
    val name: String
)

enum class Status {
    INACTIVE,
    ACTIVE
}