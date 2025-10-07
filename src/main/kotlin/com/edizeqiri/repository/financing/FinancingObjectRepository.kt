package com.edizeqiri.repository.financing

import com.edizeqiri.entity.FinancingObject

interface FinancingObjectRepository {

    fun findAllByUserId(userId: String): List<FinancingObject>
}