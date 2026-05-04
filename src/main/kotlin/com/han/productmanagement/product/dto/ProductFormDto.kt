package com.han.productmanagement.product.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class ProductFormDto(
    var id: Long? = null,

    @field:NotBlank(message = "Title is required.")
    var title: String = "",

    @field:NotNull(message = "Product type is required.")
    var productTypeId: Long? = null,

    @field:NotBlank(message = "Vendor is required.")
    var vendor: String = "",

    var bodyHtml: String = "",

    @field:Valid
    var variants: MutableList<VariantFormDto> = mutableListOf(),
) {
    val availableVariantCount: Int
        get() = variants.count { it.available }
}
