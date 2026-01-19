package com.team.prezel.buildlogic.convention.external

import org.gradle.api.Project
import java.io.StringReader
import java.util.Properties

fun Project.localProperty(key: String): String {
    val localPropertiesFile = isolated.rootProject.projectDirectory.file("local.properties")

    return providers.provider {
        val file = localPropertiesFile.asFile
        if (!file.exists()) return@provider null

        val properties = Properties()
        properties.load(StringReader(file.readText()))
        properties.getProperty(key)
    }.get()
}
