plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.badge.impl"
}

dependencies {
    implementation(projects.coreModel)
    implementation(projects.coreDomain)

    implementation(projects.featureBadgeApi)

    implementation(libs.kotlinx.collections.immutable)
}
