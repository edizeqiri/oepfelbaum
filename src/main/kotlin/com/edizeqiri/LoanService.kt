package com.edizeqiri

import com.edizeqiri.dto.Loan
import com.edizeqiri.dto.LoanCollateralInner
import com.edizeqiri.entity.FinancingObject
import com.edizeqiri.entity.toCollateralInnerType
import com.edizeqiri.entity.toLoanStatus
import com.edizeqiri.repository.FinancingObjectRepository
import com.edizeqiri.repository.LimitRepository
import com.edizeqiri.repository.ProductRepository
import jakarta.enterprise.context.ApplicationScoped
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.util.*


@ApplicationScoped
class LoanService(
    val financingObjectRepository: FinancingObjectRepository,
    val productRepository: ProductRepository,
    val limitRepository: LimitRepository
) {

    /*
    1. Get all FO's
    2. Foreach get the product and limits to match Loan
     */
    fun getAllLoansByUser(userId: String): List<Loan> {


        val financingObjects: List<FinancingObject> = financingObjectRepository.findAllByUserId(userId)
        var loans = mutableListOf<Loan>()
        financingObjects.forEach { financingObject ->
            val sum = sumParent(financingObject)
            val products = productRepository.findAllById(financingObject.products)
            val limit = limitRepository.findById(financingObject.limit)


            products.forEach { product ->
                loans.add(
                    Loan(
                        id = createUUID(financingObject, true, product.id).toString(),
                        loanType = Loan.LoanType.CHILD_LOAN,
                        name = product.name,
                        contractNumber = limit.contractNumber,
                        loanStatus = financingObject.status.toLoanStatus(),
                        currencyCode = product.currencyCode.toString(),
                        outstandingAmount = product.amount.toString(),
                        creditLimit = limit.limitAmount.toString(),
                        interestRate = product.interestRate.toString(),
                        interestDue = product.interestDue.toString(),
                        isOverdue = product.isOverdue,
                        parentLoanId = financingObject.id.toString(),
                        startDate = OffsetDateTime.from(product.startDate),
                        endDate = OffsetDateTime.from(product.endDate),
                        borrower = financingObject.owner.map { owner -> owner.name },
                        defaultSettlementAccountNumber = product.defaultSettlementAccountNumber,
                        paymentFrequency = limit.agreedAmortisationFrequency.toString(),
                        interestPaymentFrequency = product.interestPaymentFrequency.toString(),
                        collateral = listOf(
                            LoanCollateralInner(
                                type = limit.toCollateralInnerType(),
                                currentValue = limit.realSecurities.collateralValue.toString(),
                                currencyCode = limit.realSecurities.currency.toString(),
                                specification = limit.realSecurities.address,
                                nextRevaluationDate = limit.realSecurities.nextRevaluationDate,
                                amortisationPaymentAmount = limit.amortisationAmountAnnual.toString()
                            )
                        )
                    )
                )
            }
        }
        return loans
    }

    fun sumParent(financingObject: FinancingObject): Long {
        val products = financingObject.products
        return productRepository.sum(products)
    }

    fun createUUID(financingObject: FinancingObject, child: Boolean, productId: Long?): UUID {
        var encoding = "${financingObject.id}|${financingObject.limit}"
        if (child) encoding += "|${productId ?: ""}"
        return UUID.nameUUIDFromBytes(encoding.toByteArray(StandardCharsets.UTF_8))
    }

}