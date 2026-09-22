package com.example.order

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody
import au.com.dius.pact.consumer.dsl.PactBuilder
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "product-service")
class ProductClientPactTest {

    @Pact(consumer = "order-service")
    fun existingProduct(builder: PactBuilder): V4Pact =
        builder
            .usingLegacyDsl()
            .given("product with ID 1 exists")
            .uponReceiving("a request for product 1")
            .path("/products/1")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(mapOf("Content-Type" to "application/json"))
            .body(
                newJsonBody {
                    it.integerType("id", 1)
                    it.stringType("name", "Keyboard")
                    it.decimalType("price", BigDecimal("49.90"))
                }.build(),
            )
            .toPact(V4Pact::class.java)

    @Pact(consumer = "order-service")
    fun missingProduct(builder: PactBuilder): V4Pact =
        builder
            .usingLegacyDsl()
            .given("product with ID 99 does not exist")
            .uponReceiving("a request for product 99")
            .path("/products/99")
            .method("GET")
            .willRespondWith()
            .status(404)
            .toPact(V4Pact::class.java)

    @Test
    @PactTestFor(pactMethod = "existingProduct")
    fun `returns the product when it exists`(mockServer: MockServer) {
        val product = ProductClient(mockServer.getUrl()).findProduct(1)

        assertThat(product).isEqualTo(Product(1, "Keyboard", BigDecimal("49.90")))
    }

    @Test
    @PactTestFor(pactMethod = "missingProduct")
    fun `returns null when the product does not exist`(mockServer: MockServer) {
        val product = ProductClient(mockServer.getUrl()).findProduct(99)

        assertThat(product).isNull()
    }
}
