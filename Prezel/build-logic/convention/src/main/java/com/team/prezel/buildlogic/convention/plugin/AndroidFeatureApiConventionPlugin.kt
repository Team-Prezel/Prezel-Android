package com.team.prezel.buildlogic.convention.plugin

import com.team.prezel.buildlogic.convention.internal.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "prezel.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                "api"(libs.findLibrary("androidx.navigation3.runtime").get())
            }
        }
    }
}
