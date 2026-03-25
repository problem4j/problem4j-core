import org.jetbrains.gradle.ext.Application
import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.JUnit
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings

plugins {
    id("org.jetbrains.gradle.plugin.idea-ext")
}

idea {
    project {
        settings {
            runConfigurations {
                create<Gradle>("Clean [problem4j-core]") {
                    taskNames = listOf("clean")
                    projectPath = rootProject.rootDir.absolutePath
                }
                create<Gradle>("Build [problem4j-core]") {
                    taskNames = listOf("spotlessApply build")
                    projectPath = rootProject.rootDir.absolutePath
                }
                create<Gradle>("Format Code [problem4j-core]") {
                    taskNames = listOf("spotlessApply")
                    projectPath = rootProject.rootDir.absolutePath
                }
                create<JUnit>("JUnit [problem4j-core]") {
                    moduleName = "problem4j-core.test"
                    workingDirectory = rootProject.rootDir.absolutePath
                    packageName = "io.github.problem4j.core"
                }
            }
        }
    }
}
