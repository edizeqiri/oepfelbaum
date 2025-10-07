package com.edizeqiri.entity

import com.edizeqiri.dto.Loan

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

enum class Status() {
    INACTIVE,
    ACTIVE
}

fun Status.toLoanStatus(): Loan.LoanStatus =
    when (this) {
        Status.ACTIVE -> Loan.LoanStatus.ACTIVE
        Status.INACTIVE -> Loan.LoanStatus.INACTIVE
    }
