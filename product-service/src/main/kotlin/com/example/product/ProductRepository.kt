package com.example.product

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

@Component
class ProductRepository {
    private val products = ConcurrentHashMap<Long, Product>().apply {
        put(1, Product(1, "Keyboard", BigDecimal("49.90")))
        put(2, Product(2, "Mouse", BigDecimal("19.99")))
    }

    fun findById(id: Long): Product? = products[id]

    fun save(product: Product) {
        products[product.id] = product
    }

    fun clear() = products.clear()
}
