package com.edizeqiri.repository.financing

import com.edizeqiri.entity.FinancingObject

interface FinancingObjectRepository {

    fun findByUserId(userId: String): FinancingObject
}