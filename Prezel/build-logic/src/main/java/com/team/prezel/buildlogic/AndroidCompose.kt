package com.team.prezel.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.0"
        }

        dependencies {
            "implementation"("androidx.compose.ui:ui:1.5.0")
            "implementation"("androidx.compose.ui:ui-tooling-preview:1.5.0")
            "debugImplementation"("androidx.compose.ui:ui-tooling:1.5.0")
            "androidTestImplementation"("androidx.compose.ui:ui-test-junit4:1.5.0")
        }
    }
}
