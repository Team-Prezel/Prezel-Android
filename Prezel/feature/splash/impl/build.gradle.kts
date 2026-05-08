plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.splash.impl"
}

dependencies {
    implementation(projects.coreDomain)
    implementation(projects.coreModel)
    implementation(projects.featureSplashApi)
    implementation(projects.featureLoginApi)
    implementation(projects.featureTermsApi)
    implementation(projects.featureHomeApi)
    implementation(projects.featureProfileApi)
}
