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
                "bootRun"
            )
        ) { taskName ->
            val projectDir = javaClass.getResource("/$taskName-task")!!.toURI().let(::File)

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
                VAR2: override VAR2
                VAR3: three
            """.trimIndent()
            result.output shouldContainOnlyOnce expectedOutput
        }
    }
})
