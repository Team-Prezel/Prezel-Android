plugins {
    alias(libs.plugins.prezel.jvm.library)
}

dependencies {
    implementation(projects.coreModel)
    implementation(libs.javax.inject)
}
