plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.home.impl"
}

dependencies {
    implementation(projects.coreDomain)
    implementation(projects.coreModel)
    implementation(projects.featureHomeApi)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
}
