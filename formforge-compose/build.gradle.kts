plugins {
    id("formforge.android-library")
}

android {
    namespace = "com.wassimbeltaief.formforge.compose"
}

dependencies {
    implementation(project(":formforge-core"))
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
}
