package com.team.prezel.buildlogic.convention

import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Provider
import java.io.StringReader
import java.util.Properties

private val logger: Logger = Logging.getLogger("LocalProperties")

fun Project.localProperty(key: String): Provider<String> {
    val localPropertiesFile = isolated.rootProject.projectDirectory.file("local.properties")

    return providers.provider {
        val file = localPropertiesFile.asFile
        if (!file.exists()) {
            logger.warn("local.properties not found")
            return@provider null
        }

        val properties = Properties()
        properties.load(StringReader(file.readText()))
        val value = properties.getProperty(key)

        if (value == null) {
            logger.warn("Key '$key' not found in local.properties")
        }

        value
    }
}
