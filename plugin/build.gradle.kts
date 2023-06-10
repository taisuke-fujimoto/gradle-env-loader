plugins {
    id("com.gradle.plugin-publish")
    id("my.kotlin-base")
    id("my.test-task")
    id("my.functional-test-task")
}

group = "io.github.taisuke-fujimoto"
version = "0.0.1"

tasks.jar {
    manifest {
        attributes("Implementation-Version" to version)
    }
}

gradlePlugin {
    website.set("https://github.com/taisuke-fujimoto/gradle-env-loader")
    vcsUrl.set("https://github.com/taisuke-fujimoto/gradle-env-loader.git")

    plugins {
        register("envLoader") {
            id = "io.github.taisuke-fujimoto.env-loader"
            displayName = "env-loader-plugin"
            description = "Load task runtime environment variables from env files."
            tags.set(setOf("environment variables", "dotenv", "runtime", "development"))

            implementationClass = "io.github.taisuke_fujimoto.gradle.plugin.envLoader.EnvLoaderPlugin"
        }
    }
}
