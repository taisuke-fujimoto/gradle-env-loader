package my

plugins {
    `jvm-test-suite`
}

private val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()

            dependencies {
                implementation(platform(libs.findLibrary("kotest-bom").get().get().toString()))
                implementation("io.kotest:kotest-runner-junit5")
                implementation("io.kotest:kotest-framework-datatest")
            }
        }
    }
}
