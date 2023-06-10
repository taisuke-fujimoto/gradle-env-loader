package tests

import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainOnlyOnce
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

class DefaultConfigurationTest : FreeSpec({
    "default tasks can load environment variables from the env file." - {
        withData(
            listOf(
                "run",
                "bootRun",
                "azureFunctionsRun"
            )
        ) { taskName ->
            val projectDir = javaClass.getResource("/default-tasks")!!.toURI().let(::File)

            // when
            val result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .withArguments(taskName)
                .build()
            println(result.output)

            // then
            result.task(":$taskName")!!.outcome shouldBe TaskOutcome.SUCCESS

            val expectedOutput = """
                VAR1: one
                VAR2: overridden from .env.$taskName
                VAR3: three
            """.trimIndent()
            result.output shouldContainOnlyOnce expectedOutput
        }
    }

    "`org.springframework.boot` plugin's :bootRun can load environment variables from the env file." {
        val projectDir = javaClass.getResource("/spring-boot-app")!!.toURI().let(::File)

        // when
        val result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectDir)
            .withArguments("bootRun")
            .build()
        println(result.output)

        // then
        result.task(":bootRun")!!.outcome shouldBe TaskOutcome.SUCCESS

        val expectedOutput = """
            This is
            a spring boot application
        """.trimIndent()
        result.output shouldContainOnlyOnce expectedOutput
    }
})
