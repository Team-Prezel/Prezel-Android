import com.android.build.api.variant.BuildConfigField
import java.io.StringReader
import java.util.Properties

plugins {
    alias(libs.plugins.prezel.android.library)
    alias(libs.plugins.prezel.hilt)
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

    // Ktorfit
    implementation(libs.ktorfit.lib)
    ksp(libs.ktorfit.ksp)

    testImplementation(libs.kotlinx.coroutines.test)
}

val backendUrl = providers
    .fileContents(
        isolated.rootProject.projectDirectory.file("local.properties"),
    ).asText
    .map { text: String ->
        val properties = Properties()
        properties.load(StringReader(text))
        properties.getProperty("BACKEND_URL")
    }.orElse("http://example.com")

androidComponents {
    onVariants {
        it.buildConfigFields!!.put(
            "BACKEND_URL",
            backendUrl.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )
    }
}
