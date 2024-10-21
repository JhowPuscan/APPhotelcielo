pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Esto asegura que solo se usen repos de settings.gradle.kts
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "hotelcielo"
include(":app")
