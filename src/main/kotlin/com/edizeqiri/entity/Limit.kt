package com.edizeqiri.entity

import com.edizeqiri.dto.LoanCollateralInner
import java.time.LocalDate
import java.util.*

data class Limit(
    val id: Long,
    val name: String,
    val type: LimitTypes,
    val limitAmount: Double,
    val amortisationAmountAnnual: Double,
    val agreedAmortisationFrequency: Int,
    val contractNumber: String,
    val realSecurities: RealSecurities
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
    when (this.realSecurities.type) {
        "Einfamilienhaus" -> LoanCollateralInner.Type.EINFAMILIENHAUS
        "Reihenhaus" -> LoanCollateralInner.Type.REIHENHAUS
        "Stockwerkeigentum" -> LoanCollateralInner.Type.STOCKWERKEIGENTUM
        "Mehrfamilienhaus" -> LoanCollateralInner.Type.MEHRFAMILIENHAUS
        "Gartenhaus" -> LoanCollateralInner.Type.GARTENHAUS
        "Bauland" -> LoanCollateralInner.Type.BAULAND
    }
