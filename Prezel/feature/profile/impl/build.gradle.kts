plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.profile.impl"
}

dependencies {
    implementation(projects.coreDomain)
    implementation(projects.coreModel)

    implementation(projects.featureProfileApi)
    implementation(projects.featureHomeApi)
}
