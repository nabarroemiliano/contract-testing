package com.example.order

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import java.math.BigDecimal

data class Product(val id: Long, val name: String, val price: BigDecimal)

@Component
class ProductClient(@Value("\${product-service.url}") baseUrl: String) {
    private val restClient = RestClient.create(baseUrl)

    fun findProduct(id: Long): Product? =
        try {
            restClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(Product::class.java)
        } catch (e: HttpClientErrorException.NotFound) {
            null
        }
}
