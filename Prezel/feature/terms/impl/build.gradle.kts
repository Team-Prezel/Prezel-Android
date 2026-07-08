plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.terms.impl"
}

dependencies {
    implementation(projects.coreDomain)
    implementation(projects.coreModel)

    implementation(projects.featureTermsApi)
    implementation(projects.featureProfileApi)

    implementation(libs.kotlinx.collections.immutable)
}
