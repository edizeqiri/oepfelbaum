package com.edizeqiri.repository

import com.edizeqiri.entity.FinancingObject

class FinancingObjectRepository: LoanRepository<FinancingObject> {

    fun findAllByUserId(userId: String): List<FinancingObject> {
        TODO("Not yet implemented")
    }

}