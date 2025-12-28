plugins {
    id("internal.convention-java-library")
    id("internal.convention-publishing")
    alias(libs.plugins.nmcp)
}

dependencies {
    testImplementation(platform(libs.junit.bom))

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    testImplementation(libs.assertj.core)
}

internalPublishing {
    displayName = "Problem4J Core"
    description = "Core library implementing Problem model according to RFC7807 (aka RFC9457)."
}
