plugins {
    id("internal.common-convention")
    id("java-library")
}

// This convention plugin adds compilation of module-info.java with Java 9 transforms output into a multi-release JAR
// for supporting modules if used by Java 9+.

val sourceSets = extensions.getByType<SourceSetContainer>()

val main9SourceSet = sourceSets.create("main9") {
    java.srcDirs("src/main9/java", "src/main/java")
}

configurations.named(main9SourceSet.compileClasspathConfigurationName) {
    extendsFrom(configurations.named("compileClasspath").get())
}
configurations.named(main9SourceSet.runtimeClasspathConfigurationName) {
    extendsFrom(configurations.named("runtimeClasspath").get())
}

tasks.named<JavaCompile>("compileMain9Java") {
    javaCompiler = javaToolchains.compilerFor { languageVersion = JavaLanguageVersion.of(9) }
}

tasks.named<Jar>("jar") {
    into("META-INF/versions/9") {
        from(main9SourceSet.output) {
            include("module-info.class")
        }
    }
    manifest {
        attributes["Multi-Release"] = "true"
    }
}
