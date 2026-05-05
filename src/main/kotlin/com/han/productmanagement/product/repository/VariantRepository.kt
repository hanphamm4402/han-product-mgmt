package com.han.productmanagement.product.repository

import com.han.productmanagement.product.entity.Variant
import org.springframework.data.jpa.repository.JpaRepository

interface VariantRepository : JpaRepository<Variant, Long>
