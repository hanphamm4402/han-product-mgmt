package com.han.productmanagement.integration

import com.han.productmanagement.product.FammeClientPort
import com.han.productmanagement.product.ProductDto
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class FammeClient(
    private val restClient: RestClient,
) : FammeClientPort {
    override fun fetchProducts(): List<ProductDto> {
        return emptyList()
    }
}
