plugins {
    alias(libs.plugins.prezel.jvm.library)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
