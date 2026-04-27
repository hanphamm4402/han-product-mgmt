package com.han.productmanagement.product

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
) {
    @GetMapping
    fun getProducts(model: Model): String {
        model.addAttribute("products", productService.getProducts())
        return "fragments/product-table :: productTable"
    }
}
