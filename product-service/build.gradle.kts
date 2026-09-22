dependencies {
    testImplementation("au.com.dius.pact.provider:junit5:4.7.5")
}

tasks.test {
    dependsOn(":order-service:test")
}
