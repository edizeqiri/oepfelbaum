package com.edizeqiri

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response


@ApplicationScoped
@Path("/service/v1/loansByUser")
class LoanResource(
    val loanService: LoanService
) {

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun loansByUser(@PathParam("userId") userId: String): Response {

        return try {
            val loans = loanService.getAllLoansByUser(userId)
            Response.status(Response.Status.OK).entity(loans).build()
        } catch (e: Exception) {
            Log.error("Error when processing the following userID: $userId, $e")
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An unexpected error occurred").build()
        }

    }


}