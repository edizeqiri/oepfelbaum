package com.edizeqiri.entity

import java.time.LocalDate
import java.util.*

data class Product(
    val id: Long,
    val name: String,
    val type: String, //ProductTypes,
    val amount: Long,
    val currencyCode: Currency,
    val interestRate: Double,
    val startDate: LocalDate,
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
