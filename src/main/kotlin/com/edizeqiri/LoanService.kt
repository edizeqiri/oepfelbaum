package com.edizeqiri

import com.edizeqiri.dto.Loan
import com.edizeqiri.dto.LoanCollateralInner
import com.edizeqiri.entity.FinancingObject
import com.edizeqiri.entity.toCollateralInnerType
import com.edizeqiri.entity.toLoanStatus
import com.edizeqiri.repository.FinancingObjectRepository
import com.edizeqiri.repository.LimitRepository
import com.edizeqiri.repository.ProductRepository
import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import java.nio.charset.StandardCharsets
import java.time.ZoneOffset
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
        Log.info("fetched FOs: ${financingObjects.size}")
        var loans = mutableListOf<Loan>()
        financingObjects.forEach { financingObject ->
            val sum = sumParent(financingObject)
            val products = productRepository.findAllById(financingObject.products)
            Log.info("fetched products: ${products.size}")
            val limits = limitRepository.findById(financingObject.limit)
            val limit = limits.first()
            Log.info("fetched loan: ${limit}")


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
                        startDate = product.startDate.atStartOfDay().atOffset(ZoneOffset.UTC),
                        endDate = product.endDate?.atStartOfDay()?.atOffset(ZoneOffset.UTC),
                        borrower = financingObject.owners.map { owner -> owner.name },
                        defaultSettlementAccountNumber = product.defaultSettlementAccountNumber,
                        paymentFrequency = limit.agreedAmortisationFrequency.toString(),
                        interestPaymentFrequency = product.interestPaymentFrequency.toString(),
                        collateral = listOf(
                            LoanCollateralInner(
                                type = limit.toCollateralInnerType(),
                                currentValue = limit.realSecurities.first().collateralValue.toString(),
                                currencyCode = limit.realSecurities.first().currency.toString(),
                                specification = limit.realSecurities.first().address,
                                nextRevaluationDate = limit.realSecurities.first().nextRevaluationDate,
                                amortisationPaymentAmount = limit.amortisationAmountAnnual.toString()
                            )
                        )
                    )
                )
            }

            loans.add(
                Loan(
                    id = createUUID(financingObject, false, null).toString(),
                    loanType = Loan.LoanType.PARENT_LOAN,
                    outstandingAmount = sumParent(financingObject).toString()

                )
            )

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