package com.han.productmanagement.product.repository

import com.han.productmanagement.product.entity.Product
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProductRepository : JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = ["productType"])
    @Query("SELECT DISTINCT p FROM Product p ORDER BY p.updatedAt DESC, p.id DESC")
    fun findAllWithDetails(): List<Product>

    @EntityGraph(attributePaths = ["productType"])
    fun findByTitleContainingIgnoreCaseOrderByUpdatedAtDescIdDesc(title: String): List<Product>

    @EntityGraph(attributePaths = ["productType", "variants"])
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    fun findWithDetailsById(@Param("id") id: Long): Product?

    @Query(value = "SELECT NEXTVAL('product_id_seq')", nativeQuery = true)
    fun nextId(): Long
}
