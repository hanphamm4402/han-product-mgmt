package com.han.productmanagement.product

import com.han.productmanagement.common.exception.ApplicationException
import com.han.productmanagement.product.dto.ExternalProductDto
import com.han.productmanagement.product.dto.ExternalVariantDto
import com.han.productmanagement.product.dto.ProductFormDto
import com.han.productmanagement.product.dto.VariantFormDto
import com.han.productmanagement.product.integration.FammeClientPort
import com.han.productmanagement.product.repository.ProductRepository
import com.han.productmanagement.product.service.ProductService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import java.math.BigDecimal

@SpringBootTest
class ProductServiceTests @Autowired constructor(
    private val productService: ProductService,
    private val productRepository: ProductRepository,
    private val fakeFammeClient: FakeFammeClient,
) {
    @BeforeEach
    fun setUp() {
        productRepository.deleteAll()
        fakeFammeClient.products = emptyList()
    }

    @Test
    fun `product types are seeded from sample data`() {
        val types = productService.listProductTypes().map { it.name }

        assertEquals(true, "Shorts" in types)
        assertEquals(true, "Leggings" in types)
        assertEquals(13, types.size)
    }

    @Test
    fun `import saves at most fifty products with variants`() {
        fakeFammeClient.products = (1..55).map { externalProduct(it.toLong()) }

        val imported = productService.importProductsFromSource()
        val products = productService.listProducts()

        assertEquals(50, imported)
        assertEquals(50, products.size)
        assertEquals(1, products.first().variantCount)
    }

    @Test
    fun `import creates new products instead of updating previous external products`() {
        fakeFammeClient.products = listOf(externalProduct(1, title = "Initial title", variantTitle = "Initial variant"))
        productService.importProductsFromSource()
        val initialProductId = productService.listProducts().single().id

        fakeFammeClient.products = listOf(externalProduct(1, title = "Updated title", variantTitle = "Updated variant"))
        productService.importProductsFromSource()

        val products = productService.listProducts()
        assertEquals(2, products.size)
        assertEquals("Initial title", productService.getProductForm(initialProductId).title)
        assertEquals(true, products.any { it.title == "Updated title" })
    }

    @Test
    fun `import uses internal product ids instead of external product ids`() {
        fakeFammeClient.products = listOf(externalProduct(999))

        productService.importProductsFromSource()

        val importedProduct = productService.listProducts().single()
        assertEquals(false, importedProduct.id == 999L)
    }

    @Test
    fun `import skips products with unseeded product types`() {
        fakeFammeClient.products = listOf(
            externalProduct(1),
            externalProduct(2, productType = ""),
            externalProduct(3, productType = "Unknown"),
        )

        val imported = productService.importProductsFromSource()

        assertEquals(1, imported)
        assertEquals(1, productService.listProducts().size)
    }

    @Test
    fun `create requires at least one variant`() {
        val form = ProductFormDto(
            title = "Manual product",
            productTypeId = 1,
            vendor = "Manual vendor",
            variants = mutableListOf(),
        )

        assertThrows(ApplicationException::class.java) {
            productService.saveProduct(form)
        }
    }

    @Test
    fun `create update and delete product`() {
        val productId = productService.saveProduct(
            ProductFormDto(
                title = "Manual product",
                productTypeId = 1,
                vendor = "Manual vendor",
                variants = mutableListOf(
                    VariantFormDto(title = "Default", sku = "MANUAL-1", price = BigDecimal("10")),
                ),
            ),
        )

        val existing = productService.getProductForm(productId)
        existing.title = "Updated manual product"
        existing.variants.single().price = BigDecimal("20")
        productService.saveProduct(existing)

        assertEquals("Updated manual product", productService.getProductForm(productId).title)

        productService.deleteProduct(productId)

        assertEquals(false, productRepository.existsById(productId))
    }

    @TestConfiguration
    class FakeFammeConfig {
        @Bean
        @Primary
        fun fakeFammeClient(): FakeFammeClient = FakeFammeClient()
    }
}

class FakeFammeClient : FammeClientPort {
    var products: List<ExternalProductDto> = emptyList()

    override fun fetchProducts(): List<ExternalProductDto> = products
}

private fun externalProduct(
    id: Long,
    title: String = "Product $id",
    variantTitle: String = "Variant $id",
    productType: String = "Shorts",
): ExternalProductDto = ExternalProductDto(
    id = id,
    title = title,
    productType = productType,
    bodyHtml = "<p>Description</p>",
    createdAt = "2026-04-23T13:46:41+02:00",
    updatedAt = "2026-04-29T18:30:41+02:00",
    vendor = "Famme",
    variants = listOf(
        ExternalVariantDto(
            id = id * 100,
            title = variantTitle,
            sku = "SKU-$id",
            available = true,
            price = "100",
            productId = id,
            createdAt = "2026-04-23T13:46:41+02:00",
            updatedAt = "2026-04-29T18:30:41+02:00",
        ),
    ),
)
