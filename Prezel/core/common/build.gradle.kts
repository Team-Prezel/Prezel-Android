plugins {
    alias(libs.plugins.prezel.jvm.library)
    alias(libs.plugins.prezel.hilt)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
