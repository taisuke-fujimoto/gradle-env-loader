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
