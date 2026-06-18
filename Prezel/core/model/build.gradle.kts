plugins {
    alias(libs.plugins.prezel.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
}
