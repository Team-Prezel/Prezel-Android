plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.history.impl"
}

dependencies {
    implementation(projects.coreModel)
    implementation(projects.featureHistoryApi)

    implementation(libs.kotlinx.collections.immutable)
}
