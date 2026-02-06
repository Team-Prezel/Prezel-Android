pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "Prezel"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeAuto(
    ":app",
    ":core:data",
    ":core:designsystem",
    ":core:network",
    ":core:navigation",
    ":feature:home:api",
    ":feature:home:impl",
    ":feature:history:api",
    ":feature:history:impl",
)

/**
 * 모듈 경로 규칙
 * - Gradle project path:  :core-data
 * - Directory path:       core/data
 *
 * Examples
 * :core:data         -> include(":core-data"), dir = core/data
 * :feature:login:api -> include(":feature-login-api"), dir = feature/login/api
 * :feature:main      -> include(":feature-main"), dir = feature/main
 */
private fun Settings.includeAuto(
    vararg modulePaths: String,
    prefix: String = ":",
    delimiter: String = "/",
) {
    modulePaths.forEach { modulePath ->
        val directoryPath = modulePath
            .removePrefix(prefix)
            .replace(prefix, delimiter)

        val projectPath = prefix + directoryPath.replace(delimiter, "-")

        include(projectPath)
        project(projectPath).projectDir = file(directoryPath)
    }
}
