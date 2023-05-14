plugins {
    id("com.gradle.plugin-publish")
    id("my.kotlin-base")
}

group = "xyz.tf.gradle.plugin"
version = "0.0.2"

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
            id = "xyz.tf.env-loader"
            implementationClass = "xyz.tf.gradle.plugin.envLoader.EnvLoaderPlugin"
            displayName = "env-loader-plugin"
            description = "Load task runtime environment variables from env files."
            tags.set(setOf("environment variables", "dotenv", "runtime"))
        }
    }
}
