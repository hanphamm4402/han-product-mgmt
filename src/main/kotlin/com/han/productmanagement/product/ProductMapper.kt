package com.han.productmanagement.product

object ProductMapper {
    fun toDto(entity: ProductEntity): ProductDto = ProductDto(
        id = entity.id,
        externalId = entity.externalId,
        name = entity.name,
    )
}
