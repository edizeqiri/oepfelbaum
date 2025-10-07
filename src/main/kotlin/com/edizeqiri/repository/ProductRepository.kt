package com.edizeqiri.repository

import com.edizeqiri.entity.Product
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductRepository: LoanRepository<Product> {

    fun findAllById(id: List<Long>): List<Product>  {
        TODO()
    }

    fun sum(id: List<Long>): Long {
        TODO()
    }


}