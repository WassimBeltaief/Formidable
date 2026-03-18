plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(17)
}

val libs = the<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

dependencies {
    testRuntimeOnly(libs.findLibrary("junit5-engine").get())
}

tasks.test {
    useJUnitPlatform()
}
