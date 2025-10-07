package com.edizeqiri

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@QuarkusTest
class LoanResourceTest {

    private val url = "/service/v1/loansByUser"

    @ParameterizedTest
    @CsvSource(
        "11110003,3",
        "100006,0",
        "11110006,4"
    )
    fun `Provide loans for userId`(userId: String, amount: Int) {
        given()
            .`when`()
            .get("$url/$userId")
            .then()
            .statusCode(200)
            .body("size()", equalTo(amount))
    }

    @ParameterizedTest
    @CsvSource(

    )
    fun `Sum up outstanding amount of child loans in parent loan`(userId: String, amount: Int) {
        given()
            .`when`()
            .get("$url/$userId")
            .then()
            .statusCode(200)
            .body("size()", equalTo(amount))
    }

    @ParameterizedTest
    @CsvSource(

    )
    fun `Find lending date range based on product date ranges`() {

    }

    @ParameterizedTest
    @CsvSource(

    )
    fun `Identify if any part of a lending is overdue`() {

    }

    @ParameterizedTest
    @CsvSource(

    )
    fun `Calculate next amortisation payment amount for lending`() {

    }


    @ParameterizedTest
    @CsvSource(

    )
    fun `Identify when the next interest payment is due`() {

    }

    @ParameterizedTest
    @CsvSource(

    )
    fun `Show interest rate on the child loan`() {

    }


}