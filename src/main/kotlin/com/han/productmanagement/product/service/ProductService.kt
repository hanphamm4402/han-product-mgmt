package com.han.productmanagement.product.service

import com.han.productmanagement.common.exception.ApplicationException
import com.han.productmanagement.product.domain.ProductEntity
import com.han.productmanagement.product.domain.VariantEntity
import com.han.productmanagement.product.dto.ExternalProductDto
import com.han.productmanagement.product.dto.ProductFormDto
import com.han.productmanagement.product.dto.ProductListItemDto
import com.han.productmanagement.product.dto.ProductTypeDto
import com.han.productmanagement.product.integration.FammeClientPort
import com.han.productmanagement.product.repository.ProductRepository
import com.han.productmanagement.product.repository.ProductTypeRepository
import com.han.productmanagement.product.repository.VariantRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productTypeRepository: ProductTypeRepository,
    private val variantRepository: VariantRepository,
    private val fammeClient: FammeClientPort,
) {
    @Transactional(readOnly = true)
    fun listProducts(): List<ProductListItemDto> =
        productRepository.findAllWithDetails().map(ProductMapper::toListItem)

    @Transactional(readOnly = true)
    fun listProductTypes(): List<ProductTypeDto> =
        productTypeRepository.findAll().sortedBy { it.name }.map(ProductMapper::toTypeDto)

    @Transactional(readOnly = true)
    fun getProductForm(id: Long): ProductFormDto {
        val product = productRepository.findWithDetailsById(id)
            ?: throw ApplicationException("Product was not found.")
        return ProductMapper.toForm(product)
    }

    fun newProductForm(): ProductFormDto = ProductFormDto(
        variants = mutableListOf(),
    )

    @Transactional
    fun saveProduct(form: ProductFormDto): Long {
        validateAtLeastOneVariant(form)
        val now = OffsetDateTime.now()
        val productType = productTypeRepository.findById(requireNotNull(form.productTypeId)).orElseThrow {
            ApplicationException("Product type is required.")
        }
        val product = form.id?.let {
            productRepository.findWithDetailsById(it) ?: throw ApplicationException("Product was not found.")
        } ?: ProductEntity(id = productRepository.nextId(), createdAt = now)

        product.title = form.title.trim()
        product.productType = productType
        product.vendor = form.vendor.trim()
        product.bodyHtml = form.bodyHtml.trim().ifBlank { null }
        product.updatedAt = now

        val existingVariants = product.variants.associateBy { it.id }
        val variants = form.variants.map { variantForm ->
            val variantId = variantForm.id ?: variantRepository.nextId()
            ProductMapper.toVariantEntity(
                form = variantForm,
                id = variantId,
                now = now,
                createdAt = existingVariants[variantId]?.createdAt ?: now,
            )
        }
        product.replaceVariants(variants)
        return requireNotNull(productRepository.save(product).id)
    }

    @Transactional
    fun deleteProduct(id: Long) {
        productRepository.deleteById(id)
    }

    @Transactional
    fun importProductsFromSource(): Int {
        val products = fammeClient.fetchProducts().take(50)
        importProducts(products)
        return products.size
    }

    @Transactional
    fun importProducts(products: List<ExternalProductDto>) {
        products.take(50).forEach(::upsertExternalProduct)
    }

    private fun upsertExternalProduct(external: ExternalProductDto) {
        val productType = productTypeRepository.findByName(external.productType)
            ?: throw ApplicationException("Product type '${external.productType}' is not seeded.")
        val product = productRepository.findWithDetailsById(external.id) ?: ProductEntity(
            id = external.id,
            createdAt = parseTimestamp(external.createdAt),
        )

        product.title = external.title
        product.productType = productType
        product.bodyHtml = external.bodyHtml
        product.vendor = external.vendor
        product.createdAt = parseTimestamp(external.createdAt)
        product.updatedAt = parseTimestamp(external.updatedAt)

        val variants = external.variants.map { variant ->
            VariantEntity(
                id = variant.id,
                title = variant.title,
                sku = variant.sku,
                available = variant.available,
                price = BigDecimal(variant.price),
                createdAt = parseTimestamp(variant.createdAt),
                updatedAt = parseTimestamp(variant.updatedAt),
            )
        }
        product.replaceVariants(variants)
        productRepository.save(product)
    }

    private fun validateAtLeastOneVariant(form: ProductFormDto) {
        if (form.variants.isEmpty()) {
            throw ApplicationException("Add at least one variant before saving.")
        }
    }

    private fun parseTimestamp(value: String): OffsetDateTime =
        OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
