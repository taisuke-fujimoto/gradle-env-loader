package tests

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainOnlyOnce
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

class DefaultConfigurationTest : FreeSpec({
    ":run task can load environment variables from the env file." {
        val projectDir = javaClass.getResource("/run-task")!!.toURI().let(::File)

        // when
        val result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectDir)
            .withArguments("run")
            .build()

        // then
        result.task(":run")!!.outcome shouldBe TaskOutcome.SUCCESS

        val expectedOutput = """
            VAR1: one
            VAR2: override VAR2
            VAR3: three
        """.trimIndent()
        result.output shouldContainOnlyOnce expectedOutput
    }
})
