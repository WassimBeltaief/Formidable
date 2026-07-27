plugins {
    id("formidable.android-application")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.wassimbeltaief.formidable.sample"

    defaultConfig {
        applicationId = "com.wassimbeltaief.formidable.sample"
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":formidable-core"))
    implementation(project(":formidable-compose"))
    ksp(project(":formidable-ksp"))

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.ktx)
}
