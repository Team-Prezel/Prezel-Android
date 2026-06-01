plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.home.impl"
}

dependencies {
    implementation(projects.coreModel)
    implementation(projects.coreDomain)

    implementation(projects.featureAnalysisApi)
    implementation(projects.featureFeedbackApi)
    implementation(projects.featureHomeApi)
    implementation(projects.featurePracticeApi)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
}
