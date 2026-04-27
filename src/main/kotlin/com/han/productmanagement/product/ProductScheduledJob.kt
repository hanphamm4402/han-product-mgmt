package com.han.productmanagement.product

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProductScheduledJob(
    private val productService: ProductService,
) {
    @Scheduled(initialDelay = 0, fixedDelay = Long.MAX_VALUE)
    fun loadProducts() {
        productService.loadProductsFromSource()
    }
}
