package com.team.prezel.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 29
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            isCoreLibraryDesugaringEnabled = true
        }
    }

    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions.apply {
            languageVersion.set(KotlinVersion.KOTLIN_2_1)
            coreLibrariesVersion = "2.3.0"
            jvmTarget.set(JvmTarget.JVM_20)
            allWarningsAsErrors.set(false)
            freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
            freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
        }
    }

    dependencies {
        "coreLibraryDesugaring"("com.android.tools:desugar_jdk_libs:2.0.3")
    }
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    extensions.configure<KotlinJvmProjectExtension> {
        compilerOptions.apply {
            languageVersion.set(KotlinVersion.KOTLIN_2_1)
            coreLibrariesVersion = "2.3.0"
            jvmTarget.set(JvmTarget.JVM_20)
            allWarningsAsErrors.set(false)
            freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
            freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
        }
    }
}
