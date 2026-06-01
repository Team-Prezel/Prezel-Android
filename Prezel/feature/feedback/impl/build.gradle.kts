plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.feedback.impl"
}

dependencies {
    implementation(projects.coreCommon)
    implementation(projects.coreModel)
    implementation(projects.coreDomain)

    implementation(projects.featureFeedbackApi)
}
