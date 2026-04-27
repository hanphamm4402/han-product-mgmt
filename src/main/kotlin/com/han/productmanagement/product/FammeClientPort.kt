package com.han.productmanagement.product

interface FammeClientPort {
    fun fetchProducts(): List<ProductDto>
}
