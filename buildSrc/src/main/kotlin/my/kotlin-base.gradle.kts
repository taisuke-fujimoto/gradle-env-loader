package my

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        allWarningsAsErrors = true
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
