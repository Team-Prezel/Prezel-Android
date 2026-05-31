plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.analysis.impl"
}

dependencies {
    implementation(projects.coreAudio)
    implementation(projects.coreCommon)
    implementation(projects.coreDomain)
    implementation(projects.coreModel)
    implementation(projects.featureAnalysisApi)
    implementation(projects.featureHomeApi)
    implementation(projects.featureReportApi)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
}
