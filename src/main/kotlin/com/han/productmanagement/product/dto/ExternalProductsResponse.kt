package com.han.productmanagement.product.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExternalProductsResponse(
    val products: List<ExternalProductDto> = emptyList(),
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExternalProductDto(
    val id: Long,
    val title: String,
    @JsonProperty("product_type")
    val productType: String,
    @JsonProperty("body_html")
    val bodyHtml: String? = null,
    @JsonProperty("created_at")
    val createdAt: String,
    @JsonProperty("updated_at")
    val updatedAt: String,
    val vendor: String,
    val variants: List<ExternalVariantDto> = emptyList(),
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExternalVariantDto(
    val id: Long,
    val title: String,
    val sku: String,
    val available: Boolean,
    val price: String,
    @JsonProperty("product_id")
    val productId: Long,
    @JsonProperty("created_at")
    val createdAt: String,
    @JsonProperty("updated_at")
    val updatedAt: String,
)
