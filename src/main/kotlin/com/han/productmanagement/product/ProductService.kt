package com.han.productmanagement.product

import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val fammeClient: FammeClientPort,
) {
    fun getProducts(): List<ProductDto> = productRepository.findAll()

    fun loadProductsFromSource() {
        fammeClient.fetchProducts()
    }
}
