package com.edizeqiri.entity

import com.edizeqiri.dto.Loan
import com.fasterxml.jackson.annotation.JsonProperty

data class FinancingObject(

    val id: Long,
    val owners: List<Owner>,
    val limit: Long,
    val products: List<Long>,
    val status: Status
)

data class Owner(
    val id: Long,
    val name: String
)

enum class Status() {
    @JsonProperty("inactive")
    INACTIVE,
    @JsonProperty("active")
    ACTIVE
}

fun Status.toLoanStatus(): Loan.LoanStatus =
    when (this) {
        Status.ACTIVE -> Loan.LoanStatus.ACTIVE
        Status.INACTIVE -> Loan.LoanStatus.INACTIVE
    }
