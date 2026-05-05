package com.han.productmanagement.product.service

import com.han.productmanagement.product.entity.Product
import com.han.productmanagement.product.entity.ProductType
import com.han.productmanagement.product.entity.Variant
import com.han.productmanagement.product.dto.ProductFormDto
import com.han.productmanagement.product.dto.ProductListItemDto
import com.han.productmanagement.product.dto.ProductTypeDto
import com.han.productmanagement.product.dto.VariantFormDto
import java.time.OffsetDateTime

object ProductMapper {
    fun toListItem(entity: Product): ProductListItemDto = ProductListItemDto(
        id = requireNotNull(entity.id),
        title = entity.title,
        vendor = entity.vendor,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt,
        productTypeName = requireNotNull(entity.productType).name,
    )

    fun toForm(entity: Product): ProductFormDto = ProductFormDto(
        id = entity.id,
        title = entity.title,
        productTypeId = entity.productType?.id,
        vendor = entity.vendor,
        bodyHtml = entity.bodyHtml.orEmpty(),
        variants = entity.variants
            .sortedBy { it.id }
            .map {
                VariantFormDto(
                    id = it.id,
                    title = it.title,
                    sku = it.sku,
                    available = it.available,
                    price = it.price,
                )
            }
            .toMutableList(),
    )

    fun toTypeDto(entity: ProductType): ProductTypeDto = ProductTypeDto(
        id = requireNotNull(entity.id),
        name = entity.name,
    )

    fun toVariantEntity(
        form: VariantFormDto,
        id: Long,
        now: OffsetDateTime,
        createdAt: OffsetDateTime = now,
    ): Variant = Variant(
        id = id,
        title = form.title.trim(),
        sku = form.sku.trim(),
        available = form.available,
        price = requireNotNull(form.price),
        createdAt = createdAt,
        updatedAt = now,
    )
}
