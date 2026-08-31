plugins {
    id("formidable.cmp-library-published")
}

android {
    namespace = "com.wassimbeltaief.formidable.compose"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        disable += setOf("RememberInComposition", "FrequentlyChangingValue")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":formidable-core"))
        }
    }
}

dependencies {
    "androidTestImplementation"(platform(libs.compose.bom))
    "androidTestImplementation"(libs.compose.ui.test.junit4)
    "androidTestImplementation"(libs.androidx.test.runner)
    "androidTestImplementation"(libs.androidx.test.ext.junit)
    "androidTestImplementation"(libs.androidx.test.rules)
    "androidTestImplementation"(libs.espresso.core)
    "debugImplementation"(libs.compose.ui.test.manifest)
}
