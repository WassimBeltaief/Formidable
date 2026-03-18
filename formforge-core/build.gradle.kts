plugins {
    id("formforge.kotlin-library-published")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.bundles.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
}
