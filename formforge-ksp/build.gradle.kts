plugins {
    id("formforge.kotlin-library-published")
}

dependencies {
    implementation(project(":formforge-core"))
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)

    testImplementation(libs.bundles.junit5)
}
