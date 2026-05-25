plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.report.impl"
}

dependencies {
    implementation(projects.coreModel)
    implementation(projects.coreDomain)
    implementation(projects.coreUi)
    implementation(projects.featureReportApi)
    implementation(projects.featureHomeApi)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections.immutable)
}
