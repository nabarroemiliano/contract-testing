package com.example.product

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/products")
class ProductController(private val repository: ProductRepository) {

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: Long): Product =
        repository.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product $id not found")
}
