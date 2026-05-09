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
        maven {
            url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/")
            content {
                includeGroupByRegex("com\\.kakao.*")
            }
        }
    }
}

rootProject.name = "Prezel"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeAuto(
    ":app",
    ":core:auth",
    ":core:common",
    ":core:data",
    ":core:datastore",
    ":core:designsystem",
    ":core:domain",
    ":core:model",
    ":core:network",
    ":core:navigation",
    ":core:ui",
    ":feature:home:api",
    ":feature:home:impl",
    ":feature:history:api",
    ":feature:history:impl",
    ":feature:my:api",
    ":feature:my:impl",
    ":feature:setting:api",
    ":feature:setting:impl",
    ":feature:profile:api",
    ":feature:profile:impl",
    ":feature:login:api",
    ":feature:login:impl",
    ":feature:terms:api",
    ":feature:terms:impl",
    ":feature:splash:api",
    ":feature:splash:impl",
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
