# Contract testing with Pact.io

The following project includes a simple example of contract testing with Pact.io. It has two services:

- `product-service` exposes `GET /products/{id}` and returns `{id, name, price}` or `404`.
- `order-service` exposes `POST /orders` with `{productId, quantity}`, calls `product-service` and
  returns `{productId, productName, quantity, total}` or `404` when the product does not exist.

## Stack

Kotlin 2.3, Spring Boot 4.1, Pact JVM 4.7, Gradle 9.2.

## How the contract flow works

1. **Consumer test** (`order-service/src/test/.../ProductClientPactTest.kt`) describes what
   `order-service` expects from `product-service`: two interactions, each tied to a provider state.
   Pact starts a mock server, the real `ProductClient` is pointed at it, and the test asserts on the
   parsed result. When the test passes, Pact writes the contract to
   `pacts/order-service-product-service.json`.
2. **Provider verification** (`product-service/src/test/.../ProductProviderPactTest.kt`) boots the
   real `product-service`, reads the pact file from `pacts/`, and replays every interaction against
   it. Before each interaction it runs the matching `@State` method to put the service in the state
   the consumer assumed (seed product 1, or clear the repository).

The pact file is exchanged via the `pacts/` folder in this repo. In a real setup it would go
through a Pact Broker.

## Run the contract tests

```sh
./gradlew test
```

`:product-service:test` depends on `:order-service:test`, so the consumer always generates the pact
before the provider verifies it. Run one side only with `./gradlew :order-service:test` or
`./gradlew :product-service:test`.

## See the contract catch a breaking change

Rename the `name` field in `product-service/src/main/kotlin/com/example/product/Product.kt` to
`title`, then run `./gradlew :product-service:test`. The provider verification fails:

```
1.1) body: $ Actual map is missing the following keys: name
```

The consumer test still passes, because the consumer did not change. Revert the rename.

## Run the services

```sh
./gradlew :product-service:bootRun
./gradlew :order-service:bootRun
```

```sh
curl localhost:8081/products/1
curl -X POST localhost:8080/orders -H 'Content-Type: application/json' -d '{"productId":1,"quantity":2}'
```
