pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

// If rootProject.name matches project.name, it fails to generate output for nmcpZipAggregation task - two modules must
// not have the same name (with root included). That's the reason for rootProject.name not matching repository name.
rootProject.name = "problem4j-core-root"

include(":problem4j-core")
