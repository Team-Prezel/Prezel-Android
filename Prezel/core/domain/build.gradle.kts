plugins {
    alias(libs.plugins.prezel.jvm.library)
}

dependencies {
    implementation(projects.coreModel)
    implementation(projects.coreCommon)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
