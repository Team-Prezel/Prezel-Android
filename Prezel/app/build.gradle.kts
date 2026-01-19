plugins {
    alias(libs.plugins.prezel.android.application.compose)
    alias(libs.plugins.prezel.hilt)
}

android {
    namespace = "com.team.prezel"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.data)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.timber)

    ksp(libs.kotlin.metadata.jvm)
}
