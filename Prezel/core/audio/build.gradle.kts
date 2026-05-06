plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
}

android {
    namespace = "com.team.prezel.core.audio"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
