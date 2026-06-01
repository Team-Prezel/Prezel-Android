plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.my.impl"
}

dependencies {
    implementation(projects.coreDomain)
    implementation(projects.coreModel)

    implementation(projects.featureBadgeApi)
    implementation(projects.featureMyApi)
    implementation(projects.featureSettingApi)
    implementation(projects.featureProfileApi)

    implementation(libs.kotlinx.collections.immutable)
}
