package com.han.productmanagement.product.repository

import com.han.productmanagement.product.entity.Product
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = ["productType"])
    fun findAllByOrderByUpdatedAtDescIdDesc(): List<Product>

    @EntityGraph(attributePaths = ["productType"])
    fun findByTitleContainingIgnoreCaseOrderByUpdatedAtDescIdDesc(title: String): List<Product>

    @EntityGraph(attributePaths = ["productType", "variants"])
    fun findOneById(id: Long): Product?
}
