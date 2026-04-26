plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
}

android {
    namespace = "com.team.prezel.core.data"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.coreDatastore)
    implementation(projects.coreDomain)
    implementation(projects.coreModel)
    implementation(projects.coreNetwork)

    implementation(libs.kotlinx.coroutines.core)
}
