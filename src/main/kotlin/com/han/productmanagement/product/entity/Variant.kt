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
@Table(name = "variant")
class Variant(
    @Id
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String = "",

    @Column(name = "sku", nullable = false)
    var sku: String = "",

    @Column(name = "available", nullable = false)
    var available: Boolean = true,

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    var price: BigDecimal = BigDecimal.ZERO,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    var product: Product? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
