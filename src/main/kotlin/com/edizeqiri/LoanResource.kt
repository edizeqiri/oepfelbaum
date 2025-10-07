package com.edizeqiri

import com.edizeqiri.dto.Loan
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType


@ApplicationScoped
@Path("/service/v1/loansByUser")
class LoanResource(
    val loanService: LoanService
) {

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun loansByUser(@PathParam("userId") userId: String): List<Loan> = loanService.getAllLoansByUser(userId)


}