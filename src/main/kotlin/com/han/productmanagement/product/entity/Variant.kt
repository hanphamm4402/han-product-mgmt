package com.han.productmanagement.product.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "VARIANT")
class Variant(
    @Id
    @Column(name = "ID")
    var id: Long? = null,

    @Column(name = "TITLE", nullable = false)
    var title: String = "",

    @Column(name = "SKU", nullable = false)
    var sku: String = "",

    @Column(name = "AVAILABLE", nullable = false)
    var available: Boolean = true,

    @Column(name = "PRICE", nullable = false, precision = 19, scale = 2)
    var price: BigDecimal = BigDecimal.ZERO,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    var product: Product? = null,

    @Column(name = "CREATED_AT", nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "UPDATED_AT", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
