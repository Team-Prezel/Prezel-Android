import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.team.prezel.buildlogic.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    compileOnly(libs.android.gradleApiPlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = libs.plugins.prezel.android.application.compose
                .get()
                .pluginId
            implementationClass = "com.team.prezel.buildlogic.convention.plugin.AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = libs.plugins.prezel.android.application
                .asProvider()
                .get()
                .pluginId
            implementationClass = "com.team.prezel.buildlogic.convention.plugin.AndroidApplicationConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.prezel.android.library.compose
                .get()
                .pluginId
            implementationClass = "com.team.prezel.buildlogic.convention.plugin.AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.prezel.android.library
                .asProvider()
                .get()
                .pluginId
            implementationClass = "com.team.prezel.buildlogic.convention.plugin.AndroidLibraryConventionPlugin"
        }
        register("androidFeatureImpl") {
            id = libs.plugins.prezel.android.feature.impl
                .get()
                .pluginId
            implementationClass = "com.team.prezel.buildlogic.convention.plugin.AndroidFeatureImplConventionPlugin"
        }
        register("androidFeatureApi") {
            id = libs.plugins.prezel.android.feature.api
                .get()
                .pluginId
            implementationClass = "com.team.prezel.buildlogic.convention.plugin.AndroidFeatureApiConventionPlugin"
        }
        register("hilt") {
            id = libs.plugins.prezel.hilt
                .get()
                .pluginId
            implementationClass = "com.team.prezel.buildlogic.convention.plugin.HiltConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.prezel.jvm.library
                .get()
                .pluginId
            implementationClass = "com.team.prezel.buildlogic.convention.plugin.JvmLibraryConventionPlugin"
        }
    }
}
