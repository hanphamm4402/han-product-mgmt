package com.han.productmanagement.product.integration

import com.han.productmanagement.product.dto.ExternalProductDto

interface FammeClientPort {
    fun fetchProducts(): List<ExternalProductDto>
}
