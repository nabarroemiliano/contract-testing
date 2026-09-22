package com.example.product

import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.math.BigDecimal

@Provider("product-service")
@PactFolder("../pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductProviderPactTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var repository: ProductRepository

    @BeforeEach
    fun setTarget(context: PactVerificationContext) {
        context.target = HttpTestTarget("localhost", port)
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun verifyInteraction(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @State("product with ID 1 exists")
    fun productExists() {
        repository.save(Product(1, "Keyboard", BigDecimal("49.90")))
    }

    @State("product with ID 99 does not exist")
    fun productDoesNotExist() {
        repository.clear()
    }
}
