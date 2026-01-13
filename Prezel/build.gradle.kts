plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint) apply true
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        plugins.withId("com.android.base") {
            android.set(true)
        }
        plugins.withId("org.jetbrains.kotlin.jvm") {
            android.set(true)
        }
    }
}
