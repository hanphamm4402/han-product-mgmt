package com.han.productmanagement.product.dto

import java.time.OffsetDateTime

data class ProductListItemDto(
    val id: Long,
    val title: String,
    val vendor: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val variantCount: Int,
)
