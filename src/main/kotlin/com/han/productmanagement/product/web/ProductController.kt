package com.han.productmanagement.product.web

import com.han.productmanagement.product.dto.ProductFormDto
import com.han.productmanagement.product.service.ProductService
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ProductController(
    private val productService: ProductService,
) {
    @GetMapping("/")
    fun home(): String = "redirect:/product"

    @GetMapping("/product")
    fun list(
        @RequestParam("query", required = false) query: String?,
        model: Model,
    ): String {
        addProductListModel(model, query)
        model.addAttribute("searchQuery", query.orEmpty())
        return "product-list"
    }

    @PostMapping("/product/load")
    fun loadProducts(model: Model): String {
        runCatching { productService.importProductsFromSource() }
            .onFailure { model.addAttribute("tableError", "Products could not be loaded from Famme right now.") }
        addProductListModel(model)
        return "fragments/product-table :: productResults"
    }

    @GetMapping("/product/{id}")
    fun detail(@PathVariable id: Long, model: Model): String {
        addProductDetailModel(model, "Product Detail", productService.getProductForm(id))
        return "product-detail"
    }

    @GetMapping("/new-product")
    fun newProduct(model: Model): String {
        addProductDetailModel(model, "New Product", productService.newProductForm())
        return "product-detail"
    }

    @PostMapping("/product")
    fun create(
        @Valid @ModelAttribute("product") product: ProductFormDto,
        bindingResult: BindingResult,
        model: Model,
    ): String = save(product, bindingResult, model, "New Product")

    @PostMapping("/product/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @ModelAttribute("product") product: ProductFormDto,
        bindingResult: BindingResult,
        model: Model,
    ): String {
        product.id = id
        return save(product, bindingResult, model, "Product Detail")
    }

    @DeleteMapping("/product/{id}")
    fun delete(
        @PathVariable id: Long,
        @RequestParam("query", required = false) query: String?,
        model: Model,
    ): String {
        productService.deleteProduct(id)
        addProductListModel(model, query)
        return "fragments/product-table :: productResults"
    }

    private fun save(
        product: ProductFormDto,
        bindingResult: BindingResult,
        model: Model,
        screenTitle: String,
    ): String {
        if (bindingResult.hasErrors()) {
            addProductDetailModel(model, screenTitle, product)
            return "product-detail"
        }

        return runCatching { productService.saveProduct(product) }
            .fold(
                onSuccess = { "redirect:/product" },
                onFailure = {
                    bindingResult.reject("product.save", it.message ?: "Product could not be saved.")
                    addProductDetailModel(model, screenTitle, product)
                    "product-detail"
                },
            )
    }

    private fun addProductListModel(model: Model, query: String? = null) {
        val products = productService.listProductsByTitle(query)
        model.addAttribute("products", products)
        model.addAttribute("productCount", products.size)
    }

    private fun addProductDetailModel(model: Model, screenTitle: String, product: ProductFormDto) {
        model.addAttribute("screenTitle", screenTitle)
        model.addAttribute("product", product)
        model.addAttribute("productTypes", productService.listProductTypes())
        model.addAttribute("pageScript", "product-detail")
    }
}
