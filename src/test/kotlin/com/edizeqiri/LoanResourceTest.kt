package com.edizeqiri

import com.edizeqiri.dto.Loan
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.inject.Inject
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@QuarkusTest
class LoanResourceTest {

    @Inject
    lateinit var objectMapper: ObjectMapper

    private val url = "/service/v1/loansByUser"

    private fun sendLoanRequest(userId: String): List<Loan> {
        val response = given()
            .`when`()
            .get("$url/$userId")

        response.then().statusCode(200)
        val loans: List<Loan> =
            objectMapper.readValue(response.body.asString(), object : TypeReference<List<Loan>>() {})
        return loans
    }

    @ParameterizedTest
    @CsvSource(
        "11110003,3",
        "11110006,4"
    )
    fun `Provide loans for userId`(userId: String, numberOfLoans: Int) {
        given()
            .`when`()
            .get("$url/$userId")
            .then()
            .statusCode(200)
            .body("size()", equalTo(numberOfLoans))

    }

    @ParameterizedTest
    @CsvSource(
        "11110006,1940000, '[700000,880000,360000]'",
        "11110013,620000, '[620000]'",
    )
    fun `Sum up outstanding amount of child loans in parent loan`(
        userId: String,
        expectedAmount: String,
        childrenAmount: String
    ) {
        val childrenAmountList: List<Long> = objectMapper.readValue(childrenAmount)
        val loans: List<Loan> = sendLoanRequest(userId)
        assert(loans.first { it.loanType == Loan.LoanType.PARENT_LOAN }.outstandingAmount == expectedAmount)


        loans.zip(childrenAmountList) { loan, child ->
            assertEquals(loan.outstandingAmount, child.toString())
        }
    }


    @ParameterizedTest
    @CsvSource(
        "11110015, 2019-04-14T00:00Z, 2033-03-22T00:00Z"
    )
    fun `Find lending date range based on product date ranges`(
        userId: String,
        earliest: String,
        latest: String
    ) {

        val loans = sendLoanRequest(userId)
        assertEquals(
            loans.first { it.loanType == Loan.LoanType.PARENT_LOAN }.startDate.toString(),
            earliest,
            "start date failed"
        )
        assertEquals(
            loans.first { it.loanType == Loan.LoanType.PARENT_LOAN }.endDate.toString(),
            latest,
            "end date failed"
        )
    }

    @ParameterizedTest
    @CsvSource(
        "11110006, false, '[false,false,false]'",
        "11110009, false, '[false]'",
        "11110012, true, '[false,false,false,true]'"
    )
    fun `Identify if any part of a lending is overdue`(userId: String, isOverdue: Boolean, childrenOverdue: String) {
        val loans = sendLoanRequest(userId)
        var boolChildren: List<Boolean> = objectMapper.readValue(childrenOverdue)

        assert(loans.first { it.loanType == Loan.LoanType.PARENT_LOAN }.isOverdue == isOverdue)
        loans.zip(boolChildren).forEach { (loan, child) ->
            assert(loan.isOverdue == child)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "11110006, 1333.3333333333333",
        "11110015, 8000.0"
    )
    fun `Calculate next amortisation payment amount for lending`(userId: String, amortisationPaymentAmount: String) {
        val loans = sendLoanRequest(userId)
        assert(loans.first { it.loanType == Loan.LoanType.PARENT_LOAN }
            .collateral?.first()
            ?.amortisationPaymentAmount == amortisationPaymentAmount)
    }


//    @ParameterizedTest
//    @CsvSource(
//
//    )
//    fun `Identify when the next interest payment is due`() {
//
//    }

    @ParameterizedTest
    @CsvSource(
        "11110015, '[3.75,4.25]','[1,4]'"
    )
    fun `Show interest rate on the child loan`(userId: String, interestRates: String, interestFrequencies: String) {

        val loans = sendLoanRequest(userId)
        val parent = loans.first { it.loanType == Loan.LoanType.PARENT_LOAN }
        assert(parent.interestRate == null)
        assert(parent.interestPaymentFrequency == null)

        val interestRatesList: List<Double> = objectMapper.readValue(interestRates)
        val interestFrequenciesList: List<Int> = objectMapper.readValue(interestFrequencies)

        interestRatesList.zip(interestFrequenciesList).zip(loans) { (rate, freq), loan ->
            assert(loan.interestRate == rate.toString())
            assert(loan.interestPaymentFrequency == freq.toString())
        }


    }


}