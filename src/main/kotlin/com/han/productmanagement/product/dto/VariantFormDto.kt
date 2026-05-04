package com.han.productmanagement.product.dto

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

class VariantFormDto(
    var id: Long? = null,

    @field:NotBlank(message = "Variant title is required.")
    var title: String = "",

    @field:NotBlank(message = "SKU is required.")
    var sku: String = "",

    var available: Boolean = true,

    @field:NotNull(message = "Price is required.")
    @field:DecimalMin(value = "0", inclusive = true, message = "Price must be at least 0.")
    @field:DecimalMax(value = "100000000", inclusive = true, message = "Price must not exceed 100000000.")
    var price: BigDecimal? = null,
)
