package com.han.productmanagement.web.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PageController {
    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("products", emptyList<Any>())
        return "index"
    }
}
