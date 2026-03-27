import com.android.build.api.variant.BuildConfigField
import com.team.prezel.buildlogic.convention.external.localProperty

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

    implementation(libs.ktorfit.lib)
    ksp(libs.ktorfit.ksp)

    testImplementation(libs.kotlinx.coroutines.test)
}

androidComponents {
    onVariants { variant ->
        val buildType = variant.buildType ?: return@onVariants
        val buildConfigFields = variant.buildConfigFields ?: return@onVariants
        val baseUrlKey = "${buildType}.base.url"

        buildConfigFields.put(
            "BASE_URL",
            localProperty(baseUrlKey).map { value ->
                BuildConfigField("String", "\"$value\"", null)
            },
        )
    }
}
