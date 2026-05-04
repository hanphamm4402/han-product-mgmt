package com.han.productmanagement.product.service

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProductScheduledJob(
    private val productService: ProductService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(initialDelay = 0, fixedDelay = Long.MAX_VALUE)
    fun loadProducts() {
        runCatching { productService.importProductsFromSource() }
            .onSuccess { logger.info("Imported {} products from Famme.", it) }
            .onFailure { logger.warn("Product import failed: {}", it.message) }
    }
}
