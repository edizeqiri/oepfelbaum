package com.edizeqiri.entity

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.util.*

data class Product(
    val id: Long,
    val name: String,
    val type: String, //ProductTypes,
    val amount: Long,
    val currencyCode: Currency,
    val interestRate: Double,
    @JsonFormat(pattern = "dd.MM.yyyy")
    val startDate: LocalDate,
    @JsonFormat(pattern = "dd.MM.yyyy")
    val endDate: LocalDate?,
    val productNumber: String,
    val defaultSettlementAccountNumber: String,
    val interestDue: Double,
    val isOverdue: Boolean,
    val interestPaymentFrequency: Int
)

enum class ProductTypes {
    FESTHYPOTHEK,
}
