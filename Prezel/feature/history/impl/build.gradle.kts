plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.history.impl"
}

dependencies {
    implementation(projects.coreModel)
    implementation(projects.coreDomain)
    implementation(projects.featureHistoryApi)
    implementation(projects.featureReportApi)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
}
