package com.han.productmanagement.product.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "PRODUCT_TYPE")
class ProductTypeEntity(
    @Id
    @Column(name = "ID")
    var id: Long? = null,

    @Column(name = "NAME", nullable = false, unique = true)
    var name: String = "",
)
