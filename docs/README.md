# gradle-env-loader

A Gradle plugin that loads task runtime environment variables from env files.

## How to use

1. Apply this plugin to a project that has a target task.
   ```kotlin
   // project_has_task/build.gradle.kts
   
   plugins {
       id("xyz.tf.env-loader") version "0.0.1"
   
       // as an example, the target task is `run`
       application
   }
   
   application {
       mainClass.set("...")
   }
   ```
2. Create an env file in the project.
   ```properties
   # project_has_task/.env
   
   SAMPLE_VAR=sample value
   ```
3. Running the target task will load environment variables from the env file.
   ```shell
   ./gradlew project_has_task:run
   ```

## Configuration

```kotlin
// this configuration represents the default value
// in other words, if this is good enough, you don't need a configuration block
envLoader {
    // load environment variables from a file at this path
    // if multiple files have the same environment variable, the later file takes precedence
    //
    // `<taskName>` will be replaced with the task name
    // i.e. for a `run` task it will be `.env.run`
    envFiles(".env", ".env.<taskName>")

    // the name of the task that loads the environment variables
    // plus, the task must implement ProcessForkOptions
    taskNames("run", "bootRun", "azureFunctionsRun")

    // a flag that determines the priority of system environment variables and env file environment variables
    // if true, env file environment variable takes precedence
    overrideSystemEnvironment.set(false)
}
```

## Limitations

- The target task must implement [org.gradle.process.ProcessForkOptions](https://docs.gradle.org/current/javadoc/org/gradle/process/ProcessForkOptions.html)
