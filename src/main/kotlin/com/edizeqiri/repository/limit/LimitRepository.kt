package com.edizeqiri.repository.limit

import com.edizeqiri.entity.Limit

interface LimitRepository {
    fun findById(id: Long): Limit
}