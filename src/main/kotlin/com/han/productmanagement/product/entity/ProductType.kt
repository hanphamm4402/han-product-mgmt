package com.han.productmanagement.product.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product_type")
class ProductType(
    @Id
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
    var name: String = "",
)
