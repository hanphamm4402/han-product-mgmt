package com.han.productmanagement.product

import com.han.productmanagement.common.base.BaseRepository
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    jdbcClient: JdbcClient,
) : BaseRepository(jdbcClient) {
    fun findAll(): List<ProductDto> = emptyList()
}
