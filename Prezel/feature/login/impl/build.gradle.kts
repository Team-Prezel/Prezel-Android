import com.team.prezel.buildlogic.convention.external.localProperty

plugins {
    alias(libs.plugins.prezel.android.feature.impl)
}

android {
    namespace = "com.team.prezel.feature.login.impl"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "PRIVACY_POLICY_URL", "\"${localProperty("privacy.policy.url").get()}\"")
        buildConfigField("String", "TERMS_OF_SERVICE_URL", "\"${localProperty("terms.of.service.url").get()}\"")
    }
}

dependencies {
    implementation(projects.coreAuth)
    implementation(projects.featureLoginApi)
    implementation(projects.featureProfileApi)
    implementation(projects.featureHomeApi)
}
