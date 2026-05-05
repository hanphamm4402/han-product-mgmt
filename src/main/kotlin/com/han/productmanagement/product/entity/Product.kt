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
@Table(name = "product")
class Product(
    @Id
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String = "",

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_type_id", nullable = false)
    var productType: ProductType? = null,

    @Column(name = "body_html", columnDefinition = "text")
    var bodyHtml: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "vendor", nullable = false)
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
