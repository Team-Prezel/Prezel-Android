plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.home.impl"
}

dependencies {
    implementation(projects.featureHomeApi)
}
