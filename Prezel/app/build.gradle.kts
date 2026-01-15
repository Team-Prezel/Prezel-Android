plugins {
    alias(libs.plugins.prezel.android.application)
    alias(libs.plugins.prezel.android.application.compose)
}

android {
    namespace = "com.team.prezel"
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}
