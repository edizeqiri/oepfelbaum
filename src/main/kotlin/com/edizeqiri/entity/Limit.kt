package com.edizeqiri.entity

import com.edizeqiri.dto.LoanCollateralInner
import java.time.LocalDate
import java.util.*

data class Limit(
    val id: Long,
    val name: String,
    val type: String,
    val limitAmount: Double,
    val amortisationAmountAnnual: Double,
    val agreedAmortisationFrequency: Int,
    val contractNumber: String,
    val realSecurities: List<RealSecurities>
)

enum class LimitTypes {

}

data class RealSecurities(
    val type: String,
    val address: String,
    val collateralValue: Long,
    val currency: Currency,
    val nextRevaluationDate: LocalDate
)

fun Limit.toCollateralInnerType(): LoanCollateralInner.Type =
    when (this.realSecurities.first().type) {
        "Einfamilienhaus" -> LoanCollateralInner.Type.EINFAMILIENHAUS
        "Reihenhaus" -> LoanCollateralInner.Type.REIHENHAUS
        "Stockwerkeigentum" -> LoanCollateralInner.Type.STOCKWERKEIGENTUM
        "Mehrfamilienhaus" -> LoanCollateralInner.Type.MEHRFAMILIENHAUS
        "Gartenhaus" -> LoanCollateralInner.Type.GARTENHAUS
        "Bauland" -> LoanCollateralInner.Type.BAULAND
        else -> throw IllegalArgumentException("LoanCollateralInner.Type did not map correctly ${this.realSecurities.first().type}")
    }
