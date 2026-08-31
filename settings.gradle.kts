pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "formidable"

include(":formidable-core")
include(":formidable-ksp")
include(":formidable-compose")
include(":composeApp")
