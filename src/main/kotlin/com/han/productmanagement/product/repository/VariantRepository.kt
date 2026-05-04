package com.han.productmanagement.product.repository

import com.han.productmanagement.product.domain.VariantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface VariantRepository : JpaRepository<VariantEntity, Long> {
    @Query(value = "SELECT NEXTVAL('VARIANT_ID_SEQ')", nativeQuery = true)
    fun nextId(): Long
}
