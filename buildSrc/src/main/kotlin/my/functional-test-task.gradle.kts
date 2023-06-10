package my

plugins {
    `java-gradle-plugin`
    `jvm-test-suite`
}

private val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

testing.suites.register<JvmTestSuite>("functionalTest") {
    useJUnitJupiter()

    dependencies {
        implementation(project())
        implementation(gradleTestKit())
        implementation(platform(libs.findLibrary("kotest-bom").get().get().toString()))
        implementation("io.kotest:kotest-runner-junit5")
        implementation("io.kotest:kotest-framework-datatest")
    }

    gradlePlugin.testSourceSets.add(sources)
}
