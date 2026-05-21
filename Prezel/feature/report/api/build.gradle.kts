plugins {
    alias(libs.plugins.prezel.android.feature.api)
}

android {
    namespace = "com.team.prezel.feature.report.api"
}

dependencies {
    implementation(projects.coreModel)
    implementation(libs.kotlinx.datetime)
}
