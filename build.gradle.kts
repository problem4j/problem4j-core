import com.diffplug.spotless.LineEnding

plugins {
    id("internal.java-library-convention")
    id("internal.publishing-convention")
    alias(libs.plugins.nmcp)
    alias(libs.plugins.spotless)
}

dependencies {
    api(libs.jspecify)

    testImplementation(platform(libs.junit.bom))

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    testImplementation(libs.assertj.core)
}

// see buildSrc/src/main/kotlin/internal.publishing-convention.gradle.kts
internalPublishing {
    displayName = "Problem4J Core"
    description = "Core library implementing Problem model according to RFC7807 (aka RFC9457)."
}

nmcp {
    publishAllPublicationsToCentralPortal {
        username = System.getenv("PUBLISHING_USERNAME")
        password = System.getenv("PUBLISHING_PASSWORD")

        publishingType = "USER_MANAGED"
    }
}

spotless {
    java {
        target("**/src/**/*.java")

        // NOTE: decided not to upgrade Google Java Format, as versions 1.29+ require running it on Java 21
        googleJavaFormat("1.28.0")
        forbidWildcardImports()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    kotlin {
        target("**/src/**/*.kt")

        ktfmt("0.60").metaStyle()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    kotlinGradle {
        target("*.gradle.kts", "buildSrc/*.gradle.kts", "buildSrc/src/**/*.gradle.kts")

        ktlint("1.8.0").editorConfigOverride(mapOf("max_line_length" to "120"))
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    format("yaml") {
        target("**/*.yml", "**/*.yaml")

        trimTrailingWhitespace()
        leadingTabsToSpaces(2)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }

    format("misc") {
        target("**/.gitattributes", "**/.gitignore")

        trimTrailingWhitespace()
        leadingTabsToSpaces(4)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
}

// This module targets Java 8 for its main sources to maintain compatibility with older runtime environments used by
// dependent systems.
//
// Unit tests, however, are executed on Java 17 because JUnit 6 requires Java 17 or newer. The Gradle toolchain
// configuration ensures that only the test compilation and execution use Java 17, while the main code remains compiled
// for Java 8.
//
// In short:
//   - src/main -> Java 8 (for compatibility)
//   - src/test -> Java 17 (required by JUnit 6)

// JUnit 6 requires at Java 17+, main keeps Java 8.
tasks.named<JavaCompile>("compileTestJava") {
    javaCompiler = javaToolchains.compilerFor { languageVersion = JavaLanguageVersion.of(17) }
}

// JUnit 6 requires at Java 17+, main keeps Java 8.
tasks.withType<Test>().configureEach {
    javaLauncher = javaToolchains.launcherFor { languageVersion = JavaLanguageVersion.of(17) }
}
