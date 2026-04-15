plugins {
    alias(libs.plugins.prezel.android.library.compose)
}

android {
    namespace = "com.team.prezel.core.ui"
}

dependencies {
    implementation(projects.coreDesignsystem)
}
