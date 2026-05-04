package com.han.productmanagement.product.repository

import com.han.productmanagement.product.domain.ProductEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProductRepository : JpaRepository<ProductEntity, Long> {
    @EntityGraph(attributePaths = ["productType", "variants"])
    @Query("SELECT DISTINCT p FROM ProductEntity p ORDER BY p.updatedAt DESC, p.id DESC")
    fun findAllWithDetails(): List<ProductEntity>

    @EntityGraph(attributePaths = ["productType", "variants"])
    @Query("SELECT p FROM ProductEntity p WHERE p.id = :id")
    fun findWithDetailsById(@Param("id") id: Long): ProductEntity?

    @Query(value = "SELECT NEXTVAL('product_id_seq')", nativeQuery = true)
    fun nextId(): Long
}
