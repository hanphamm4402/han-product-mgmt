package com.han.productmanagement.product.integration

import com.han.productmanagement.product.dto.ExternalProductDto
import com.han.productmanagement.product.dto.ExternalProductsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class FammeClient(
    restClientBuilder: RestClient.Builder,
    @Value("\${app.integration.famme-base-url}") private val fammeBaseUrl: String,
) : FammeClientPort {
    private val restClient = restClientBuilder.baseUrl(fammeBaseUrl).build()

    override fun fetchProducts(): List<ExternalProductDto> {
        return restClient.get()
            .uri("/products.json")
            .retrieve()
            .body(ExternalProductsResponse::class.java)
            ?.products
            .orEmpty()
    }
}
