package com.edizeqiri

import com.edizeqiri.dto.Loan
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam


@ApplicationScoped
@Path("/loansByUser")
class LoanResource {

    @GET
    @Path("{userId}")
    fun loansByUser(@PathParam("userId") userId: String): List<Loan> {
        TODO()
    }



}