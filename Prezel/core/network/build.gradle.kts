import com.android.build.api.variant.BuildConfigField
import com.team.prezel.buildlogic.convention.localProperty

plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    buildFeatures {
        buildConfig = true
    }

    namespace = "com.team.prezel.core.network"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)

    // Ktorfit
    implementation(libs.ktorfit.lib)
    ksp(libs.ktorfit.ksp)

    testImplementation(libs.kotlinx.coroutines.test)
}

androidComponents {
    onVariants { variant ->
        val buildConfigFields = variant.buildConfigFields ?: return@onVariants
        val isRelease = variant.buildType == "release"
        // DEBUG_RELEASE_BASE_URL 또는 RELEASE_BASE_URL
        val key = "${variant.buildType!!.uppercase()}_BASE_URL"

        val urlProvider = if (isRelease) {
            localProperty(key).map {
                it.ifEmpty { throw GradleException("$key is required for release builds") }
            }
        } else {
            localProperty(key).orElse("http://10.0.2.2")
        }

        buildConfigFields.put(
            "BASE_URL",
            urlProvider.map { value ->
                BuildConfigField("String", """"$value"""", null)
            },
        )
    }
}
