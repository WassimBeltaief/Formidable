plugins {
    id("formidable.kmp-library")
    `maven-publish`
    signing
}

kotlin {
    explicitApi()
}

val groupName = providers.gradleProperty("GROUP").get()
val versionName = providers.gradleProperty("VERSION_NAME").get()

group = groupName
version = versionName

publishing {
    repositories {
        maven {
            name = "LocalStaging"
            url = uri(rootProject.layout.buildDirectory.dir("staging"))
        }
    }
}

afterEvaluate {
    publishing {
        publications.withType<MavenPublication>().configureEach {
            if (name != "kotlinMultiplatform" && name != "wasmJs") {
                val pubName = name
                val javadocJar = tasks.register("${pubName}JavadocJar", Jar::class) {
                    archiveClassifier.set("javadoc")
                    archiveAppendix.set(pubName)
                }
                artifact(javadocJar)
            }
            pom {
                name.set(project.name)
                description.set("Headless, schema-driven form engine for Jetpack Compose")
                url.set("https://github.com/WassimBeltaief/formidable")
                inceptionYear.set("2024")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("wassimbeltaief")
                        name.set("Wassim Beltaief")
                        url.set("https://github.com/WassimBeltaief")
                    }
                }

                scm {
                    url.set("https://github.com/WassimBeltaief/formidable")
                    connection.set("scm:git:git://github.com/WassimBeltaief/formidable.git")
                    developerConnection.set("scm:git:ssh://git@github.com/WassimBeltaief/formidable.git")
                }
            }
        }

    }

    signing {
        val signingKey = providers.gradleProperty("SIGNING_KEY").orNull
            ?: providers.environmentVariable("SIGNING_KEY").orNull
        val signingPassword = providers.gradleProperty("SIGNING_PASSWORD").orNull
            ?: providers.environmentVariable("SIGNING_PASSWORD").orNull

        if (signingKey != null && signingPassword != null) {
            useInMemoryPgpKeys(signingKey, signingPassword)
        }

        sign(publishing.publications)
    }

    tasks.withType<Sign>().configureEach {
        onlyIf { !version.toString().endsWith("SNAPSHOT") }
    }

    tasks.withType<PublishToMavenRepository>().configureEach {
        onlyIf { publication.name != "wasmJs" }
    }
}