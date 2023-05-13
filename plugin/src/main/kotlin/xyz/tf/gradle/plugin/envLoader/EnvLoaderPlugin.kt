package xyz.tf.gradle.plugin.envLoader

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.process.ProcessForkOptions
import xyz.tf.gradle.plugin.envLoader.internal.parseEnvContent

class EnvLoaderPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("envLoader", EnvLoaderExtension::class.java)

        val loadEnv = LoadEnv(extension)

        target.tasks.configureEach { if (it is ProcessForkOptions) it.doFirst(loadEnv) }
        target.tasks.whenTaskAdded { if (it is ProcessForkOptions) it.doFirst(loadEnv) }
    }

    private class LoadEnv(
        val extension: EnvLoaderExtension
    ) : Action<Task> {
        override fun execute(task: Task) {
            check(task is ProcessForkOptions) { "task must be ProcessForkOptions (actual type: ${task::class})" }

            val taskNames = extension.taskNames.get()
            val envFiles = extension.envFiles.get()

            if (task.name in taskNames) {
                val expandedEnvFiles = envFiles.map { it.replace("<taskName>", task.name) }
                val env = expandedEnvFiles.asSequence()
                    .map { extension.projectLayout.projectDirectory.file(it).asFile }
                    .filter { it.isFile }
                    .map { it.readText().parseEnvContent() }
                    .reduceOrNull { acc, map -> acc + map }
                    ?.let {
                        if (extension.overrideSystemEnvironment.get()) it else it - System.getenv().keys
                    }

                if (env == null) {
                    task.logger.warn("Could not read files specified in [envFiles]: $expandedEnvFiles")
                } else {
                    task.logger.trace("apply environment from [envFiles]: $expandedEnvFiles")
                    env.forEach { (name, value) -> task.environment(name, value) }
                }
            }
        }
    }
}
