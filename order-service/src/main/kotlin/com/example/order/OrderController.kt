package com.example.order

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

data class OrderRequest(val productId: Long, val quantity: Int)

data class Order(val productId: Long, val productName: String, val quantity: Int, val total: BigDecimal)

@RestController
class OrderController(private val productClient: ProductClient) {

    @PostMapping("/orders")
    fun createOrder(@RequestBody request: OrderRequest): Order {
        val product = productClient.findProduct(request.productId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product ${request.productId} not found")
        return Order(
            productId = product.id,
            productName = product.name,
            quantity = request.quantity,
            total = product.price * BigDecimal(request.quantity),
        )
    }
}
