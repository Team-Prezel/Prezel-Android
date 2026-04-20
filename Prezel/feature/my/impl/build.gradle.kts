plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.my.impl"
}

dependencies {
    implementation(projects.featureMyApi)
}
