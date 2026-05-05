package com.han.productmanagement.product.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "PRODUCT")
class Product(
    @Id
    @Column(name = "ID")
    var id: Long? = null,

    @Column(name = "TITLE", nullable = false)
    var title: String = "",

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_TYPE_ID", nullable = false)
    var productType: ProductType? = null,

    @Column(name = "BODY_HTML", columnDefinition = "TEXT")
    var bodyHtml: String? = null,

    @Column(name = "CREATED_AT", nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "UPDATED_AT", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "VENDOR", nullable = false)
    var vendor: String = "",

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    var variants: MutableList<Variant> = mutableListOf(),
) {
    fun replaceVariants(newVariants: List<Variant>) {
        variants.clear()
        newVariants.forEach { addVariant(it) }
    }

    fun addVariant(variant: Variant) {
        variant.product = this
        variants.add(variant)
    }
}
