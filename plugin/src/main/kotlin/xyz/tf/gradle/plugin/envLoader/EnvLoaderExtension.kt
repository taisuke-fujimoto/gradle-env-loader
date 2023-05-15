package xyz.tf.gradle.plugin.envLoader

import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import xyz.tf.gradle.plugin.envLoader.internal.listProperty
import xyz.tf.gradle.plugin.envLoader.internal.property
import xyz.tf.gradle.plugin.envLoader.internal.setProperty
import javax.inject.Inject

open class EnvLoaderExtension @Inject constructor(
    objectFactory: ObjectFactory,
    internal val projectLayout: ProjectLayout
) {
    internal val envFiles: ListProperty<String> = objectFactory.listProperty { listOf(".env", ".env.<taskName>") }

    fun envFiles(value: List<String>) {
        require(value.isNotEmpty()) { "[envFiles] must not be empty." }
        envFiles.set(value)
    }

    fun envFiles(value: String, vararg remainValues: String) {
        envFiles.set(
            buildList {
                add(value)
                addAll(remainValues)
            }
        )
    }

    internal val taskNames: SetProperty<String> = objectFactory.setProperty {
        setOf("run", "bootRun", "azureFunctionsRun")
    }

    fun taskNames(value: Set<String>) {
        require(value.isNotEmpty()) { "[taskNames] must not be empty." }
        taskNames.set(value)
    }

    fun taskNames(value: String, vararg remainValues: String) {
        taskNames.set(
            buildSet {
                add(value)
                addAll(remainValues)
            }
        )
    }

    val overrideSystemEnvironment: Property<Boolean> = objectFactory.property { false }
}
