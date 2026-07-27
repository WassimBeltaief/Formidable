plugins {
    id("formidable.kotlin-library")
}

kotlin {
    explicitApi()
}

val groupName = providers.gradleProperty("GROUP").get()
val versionName = providers.gradleProperty("VERSION_NAME").get()

group = groupName
version = versionName
