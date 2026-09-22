dependencies {
    testImplementation("au.com.dius.pact.consumer:junit5:4.7.5")
}

tasks.test {
    systemProperty("pact.rootDir", rootDir.resolve("pacts"))
}
