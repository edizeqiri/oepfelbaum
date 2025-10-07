package com.edizeqiri.repository.product

import com.edizeqiri.entity.Product

interface ProductRepository {
    fun findAllById(id: List<Long>): List<Product>

    fun sum(id: List<Long>): Long
}