plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.allopen") version "1.9.0"
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
// val quarkiverseCxfPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation(platform("${quarkusPlatformGroupId}:quarkus-camel-bom:${quarkusPlatformVersion}"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib") // Kotlin
    implementation("io.quarkus:quarkus-kotlin") // Kotlin for Quarkus
    implementation("org.bouncycastle:bcprov-jdk15on") // Bouncy Castle Algorithms
    implementation("io.quarkus:quarkus-smallrye-context-propagation") // Context Propagation for Reactive
    implementation("io.quarkus:quarkus-security-jpa") // HTTP RBAC
    implementation("io.quarkus:quarkus-smallrye-openapi") // Swagger UI / OpenAPI
    implementation("io.quarkus:quarkus-arc") // Context and Dependency Injection
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson") // REST API Reactive with Jackson serialization
    implementation("io.quarkus:quarkus-mailer") // SMTP support
    implementation("io.quarkus:quarkus-scheduler") // Schedule tasks
    implementation("org.apache.camel.quarkus:camel-quarkus-csv") // CSV Parser

    // External
    implementation("io.quarkus:quarkus-rest-client-reactive")
    implementation("jakarta.xml.ws:jakarta.xml.ws-api") // SOAP support
    implementation("com.sun.xml.messaging.saaj:saaj-impl") // SOAP provider
    // Not working, see issue: https://github.com/quarkiverse/quarkus-cxf/issues/916
    // implementation(enforcedPlatform("io.quarkiverse.cxf:quarkus-cxf-bom:${quarkiverseCxfPlatformVersion}"))
    // implementation("io.quarkiverse.cxf:quarkus-cxf") // SOAP API support
    // implementation("jakarta.jws:jakarta.jws-api")

    // JWT
    implementation("io.quarkus:quarkus-smallrye-jwt") // Verify JWT tokens
    implementation("io.quarkus:quarkus-smallrye-jwt-build") // Build JWT tokens

    // Database
    implementation("io.quarkus:quarkus-hibernate-reactive-panache") // Hibernate Reactive simplified
    implementation("io.quarkus:quarkus-reactive-mysql-client") // MySQL Reactive client
    implementation("io.quarkus:quarkus-jdbc-mysql") // MySQL JDBC client (for flyway)
    implementation("io.quarkus:quarkus-flyway") // Database Migration
    implementation("org.flywaydb:flyway-mysql") // Flyway MySQL client

    // Testing
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "com.casadosreclamos"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
}
