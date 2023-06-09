plugins {
    java
    // TODO: consider Spring Boot 3 + JDK 17
    id("org.springframework.boot") version "2.7.12"
    id("io.github.taisuke-fujimoto.env-loader")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter")
}
