package com.han.productmanagement.product.service

import com.han.productmanagement.common.exception.ApplicationException
import com.han.productmanagement.product.entity.Product
import com.han.productmanagement.product.entity.Variant
import com.han.productmanagement.product.dto.ExternalProductDto
import com.han.productmanagement.product.dto.ProductFormDto
import com.han.productmanagement.product.dto.ProductListItemDto
import com.han.productmanagement.product.dto.ProductTypeDto
import com.han.productmanagement.product.integration.FammeClientPort
import com.han.productmanagement.product.repository.ProductRepository
import com.han.productmanagement.product.repository.ProductTypeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productTypeRepository: ProductTypeRepository,
    private val fammeClient: FammeClientPort,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun getProducts(): List<ProductListItemDto> =
        productRepository.findAllByOrderByUpdatedAtDescIdDesc().map(ProductMapper::toListItem)

    @Transactional(readOnly = true)
    fun getProductWithQuery(query: String?): List<ProductListItemDto> {
        val title = query?.trim().orEmpty()
        val products = if (title.isBlank()) {
            productRepository.findAllByOrderByUpdatedAtDescIdDesc()
        } else {
            productRepository.findByTitleContainingIgnoreCaseOrderByUpdatedAtDescIdDesc(title)
        }
        return products.map(ProductMapper::toListItem)
    }

    @Transactional(readOnly = true)
    fun getProductTypes(): List<ProductTypeDto> =
        productTypeRepository.findAll().sortedBy { it.name }.map(ProductMapper::toTypeDto)

    @Transactional(readOnly = true)
    fun getProductById(id: Long): ProductFormDto {
        val product = productRepository.findOneById(id)
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
            productRepository.findOneById(it) ?: throw ApplicationException("Product was not found.")
        } ?: Product(createdAt = now)

        product.title = form.title.trim()
        product.productType = productType
        product.vendor = form.vendor.trim()
        product.bodyHtml = form.bodyHtml.trim().ifBlank { null }
        product.updatedAt = now

        val existingVariants = product.variants.associateBy { it.id }
        val variants = form.variants.map { variantForm ->
            ProductMapper.toVariantEntity(
                form = variantForm,
                id = variantForm.id,
                now = now,
                createdAt = variantForm.id?.let(existingVariants::get)?.createdAt ?: now,
            )
        }
        product.replaceVariants(variants)
        return requireNotNull(productRepository.save(product).id)
    }

    @Transactional
    fun deleteProductById(id: Long) {
        productRepository.deleteById(id)
    }

    @Transactional
    fun importProductsFromSource(): Int {
        val products = fammeClient.fetchProducts().take(50)
        return importProducts(products)
    }

    @Transactional
    fun importProducts(products: List<ExternalProductDto>): Int {
        return products.take(50).count(::createFromExternalProduct)
    }

    private fun createFromExternalProduct(external: ExternalProductDto): Boolean {
        val productTypeName = external.productType.trim()
        val productType = productTypeName.takeIf(String::isNotBlank)?.let(productTypeRepository::findByName)
        if (productType == null) {
            logger.debug("Skipping product {} because product type '{}' is not seeded.", external.id, productTypeName)
            return false
        }

        val product = Product(createdAt = parseTimestamp(external.createdAt))

        product.title = external.title
        product.productType = productType
        product.bodyHtml = external.bodyHtml
        product.vendor = external.vendor
        product.createdAt = parseTimestamp(external.createdAt)
        product.updatedAt = parseTimestamp(external.updatedAt)

        val variants = external.variants.map { variant ->
            Variant(
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
        return true
    }

    private fun validateAtLeastOneVariant(form: ProductFormDto) {
        if (form.variants.isEmpty()) {
            throw ApplicationException("Add at least one variant before saving.")
        }
    }

    private fun parseTimestamp(value: String): OffsetDateTime =
        OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
