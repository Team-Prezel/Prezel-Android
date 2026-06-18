plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
}

android {
    namespace = "com.team.prezel.core.data"
}

dependencies {
    implementation(projects.coreCommon)
    implementation(projects.coreDatastore)
    implementation(projects.coreDomain)
    implementation(projects.coreModel)
    implementation(projects.coreNetwork)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
}
