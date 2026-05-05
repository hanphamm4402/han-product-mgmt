package com.han.productmanagement.product.repository

import com.han.productmanagement.product.entity.ProductType
import org.springframework.data.jpa.repository.JpaRepository

interface ProductTypeRepository : JpaRepository<ProductType, Long> {
    fun findByName(name: String): ProductType?
}
