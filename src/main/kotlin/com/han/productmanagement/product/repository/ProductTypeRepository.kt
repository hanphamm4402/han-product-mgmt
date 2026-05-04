package com.han.productmanagement.product.repository

import com.han.productmanagement.product.domain.ProductTypeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductTypeRepository : JpaRepository<ProductTypeEntity, Long> {
    fun findByName(name: String): ProductTypeEntity?
}
