plugins {
    application
    id("io.github.taisuke-fujimoto.env-loader")
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("app.Main")
}

tasks.register<JavaExec>("bootRun") {
    mainClass.set("app.Main")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("azureFunctionsRun") {
    mainClass.set("app.Main")
    classpath = sourceSets["main"].runtimeClasspath
}
