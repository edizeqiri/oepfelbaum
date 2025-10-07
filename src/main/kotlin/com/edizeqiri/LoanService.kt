package com.edizeqiri

import com.edizeqiri.dto.Loan
import com.edizeqiri.dto.LoanCollateralInner
import com.edizeqiri.entity.*
import com.edizeqiri.repository.financing.FinancingObjectRepository
import com.edizeqiri.repository.limit.LimitRepository
import com.edizeqiri.repository.product.ProductRepository
import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*


@ApplicationScoped
class LoanService(
    val financingObjectRepository: FinancingObjectRepository,
    val productJsonRepository: ProductRepository,
    val limitRepository: LimitRepository
) {

    fun getAllLoansByUser(userId: String): List<Loan> {
        var financingObject: FinancingObject = try {
            financingObjectRepository.findAllByUserId(userId)
        } catch (e: NoSuchElementException) {
            Log.error("No user with the specified ID: $userId, $e")
            throw e
        }
        var loans = mutableListOf<Loan>()
        val products = productJsonRepository.findAllById(financingObject.products)
        val limit = limitRepository.findById(financingObject.limit)

        val collateral =
            LoanCollateralInner(
                type = limit.toCollateralInnerType(),
                currentValue = limit.realSecurities.first().collateralValue.toString(),
                currencyCode = limit.realSecurities.first().currency.toString(),
                specification = limit.realSecurities.first().address,
                nextRevaluationDate = limit.realSecurities.first().nextRevaluationDate,
            )

        var earliest = LocalDate.MAX
        var latest = LocalDate.MIN
        var isOverdue = false

        // children
        products.forEach { product ->

            // earliest, latest
            if (product.startDate < earliest) earliest = product.startDate
            product.endDate?.let { if (it > latest) latest = it }

            // isOverdue
            isOverdue = isOverdue.or(product.isOverdue)


            loans.add(createLoan(financingObject, product, limit, collateral))
        }

        // add parent to list
        loans.add(
            Loan(
                id = createUUID(financingObject, false, null).toString(),
                loanType = Loan.LoanType.PARENT_LOAN,
                outstandingAmount = productJsonRepository.sum(financingObject.products).toString(),
                startDate = earliest.atStartOfDay().atOffset(ZoneOffset.UTC),
                endDate = if (latest == LocalDate.MAX) null else latest.atStartOfDay().atOffset(ZoneOffset.UTC),
                isOverdue = isOverdue,
                paymentFrequency = limit.agreedAmortisationFrequency.toString(),
                collateral = listOf(
                    collateral.copy(amortisationPaymentAmount = (limit.amortisationAmountAnnual / limit.agreedAmortisationFrequency).toString())
                )
            )
        )
        return loans
    }

    private fun createLoan(
        financingObject: FinancingObject,
        product: Product,
        limit: Limit,
        collateral: LoanCollateralInner
    ): Loan = Loan(
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
        interestPaymentFrequency = product.interestPaymentFrequency.toString(),
        collateral = listOf(collateral)
    )

    fun createUUID(financingObject: FinancingObject, child: Boolean, productId: Long?): UUID {
        var encoding = "${financingObject.id}|${financingObject.limit}"
        if (child) encoding += "|${productId ?: ""}"
        return UUID.nameUUIDFromBytes(encoding.toByteArray(StandardCharsets.UTF_8))
    }
}