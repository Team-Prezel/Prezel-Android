package com.team.prezel.buildlogic.convention

import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import java.io.StringReader
import java.util.Properties

fun ProviderFactory.localProperty(
    projectDirectory: Directory,
    key: String,
    default: String? = null,
): Provider<String> =
    fileContents(projectDirectory.file("local.properties"))
        .asText
        .map { text ->
            val properties = Properties()
            properties.load(StringReader(text))
            properties.getProperty(key)
        }.let { provider ->
            if (default != null) {
                provider.orElse(default)
            } else {
                provider
            }
        }
