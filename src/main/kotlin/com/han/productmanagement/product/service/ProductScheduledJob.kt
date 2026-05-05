package com.han.productmanagement.product.service

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "app.integration", name = ["famme-import-enabled"], havingValue = "true", matchIfMissing = true)
class ProductScheduledJob(
    private val productService: ProductService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(initialDelay = 0, fixedDelay = Long.MAX_VALUE)
    fun loadProducts() {
        try {
            val importedProducts = productService.importProductsFromSource()
            logger.info("Imported {} products from Famme.", importedProducts)
        } catch (exception: Exception) {
            logger.warn("Product import failed: {}", exception.message)
        }
    }
}
