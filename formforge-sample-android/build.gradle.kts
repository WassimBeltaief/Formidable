plugins {
    id("formforge.android-application")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.wassimbeltaief.formforge.sample"

    defaultConfig {
        applicationId = "com.wassimbeltaief.formforge.sample"
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":formforge-core"))
    implementation(project(":formforge-compose"))
    ksp(project(":formforge-ksp"))

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.ktx)
}
