plugins {
    id("formidable.kotlin-library-published")
}

dependencies {
    implementation(project(":formidable-core"))
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)

    testImplementation(libs.bundles.junit5)
}
