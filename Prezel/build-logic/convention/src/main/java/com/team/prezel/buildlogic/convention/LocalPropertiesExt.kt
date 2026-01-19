package com.team.prezel.buildlogic.convention

import org.gradle.api.file.Directory
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import java.io.StringReader
import java.util.Properties

private val logger: Logger = Logging.getLogger("LocalProperties")

fun ProviderFactory.localProperty(
    projectDirectory: Directory,
    key: String,
    default: String? = null,
): Provider<String> {
    val localPropertiesFile = projectDirectory.file("local.properties")

    return provider {
        val file = localPropertiesFile.asFile
        if (!file.exists()) {
            logger.warn("local.properties not found")
            return@provider default
        }

        val properties = Properties()
        properties.load(StringReader(file.readText()))
        val value = properties.getProperty(key)

        if (value == null) {
            logger.warn("Key '$key' not found in local.properties")
            return@provider default
        }

        value
    }
}
