plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.login.impl"
}

dependencies {
    implementation(projects.coreAuth)
    implementation(projects.coreDomain)
    implementation(projects.coreModel)

    implementation(projects.featureLoginApi)
    implementation(projects.featureTermsApi)
    implementation(projects.featureProfileApi)
    implementation(projects.featureHomeApi)
}
