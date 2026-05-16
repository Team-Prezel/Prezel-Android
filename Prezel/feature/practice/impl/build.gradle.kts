plugins {
    alias(libs.plugins.prezel.android.feature.impl)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.team.prezel.feature.practice.impl"
}

dependencies {
    implementation(projects.coreAudio)
    implementation(projects.coreDomain)
    implementation(projects.coreModel)
    implementation(projects.featureHomeApi)
    implementation(projects.featurePracticeApi)
}
