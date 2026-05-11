plugins {
    alias(libs.plugins.prezel.android.library.compose)
}

android {
    namespace = "com.team.prezel.core.ui"
}

dependencies {
    implementation(projects.coreDesignsystem)
    implementation(projects.coreModel)
    implementation(libs.lottie.compose)
    implementation(libs.kotlinx.collections.immutable)
}
