package com.team.prezel.buildlogic.convention.plugin

import com.android.build.api.dsl.LibraryExtension
import com.team.prezel.buildlogic.convention.internal.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "prezel.android.library.compose")
            apply(plugin = "prezel.hilt")

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
            }

            dependencies {
//                "implementation"(project(":core:ui"))
                "implementation"(project(":core-designsystem"))
                "implementation"(project(":core-navigation"))
                "implementation"(libs.findLibrary("androidx.navigation3.ui").get())
            }
        }
    }
}
