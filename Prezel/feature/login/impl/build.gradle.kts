plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.login.impl"
}

dependencies {
    implementation(projects.coreAuth)
    implementation(projects.featureLoginApi)
    implementation(projects.featureHomeApi)
}
