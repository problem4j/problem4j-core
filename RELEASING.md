# Releasing

A release is created by pushing an annotated git tag named `v1.2.3` with the message "Release 1.2.3". Preferably, use
the [`./tools/tagrelease`](./tools/tagrelease) script, which ensures the tag is correctly formatted and prevents
mistakes. Proper tag format is required to trigger build automation.

See `./tools/tagrelease --help` for reference.

## Maven Central

[![Publish Release Status](https://github.com/problem4j/problem4j-core/actions/workflows/gradle-publish-release.yml/badge.svg)][gradle-publish-release]
[![Sonatype](https://img.shields.io/maven-central/v/io.github.problem4j/problem4j-core)][maven-central]

1. Keep Git tags with `vX.Y.Z-suffix` format. GitHub Actions job will only trigger on such tags and will remove `v`
   prefix.
2. The releasing procedure only uploads the artifacts to Sonatype repository. You need to manually log in to Sonatype to
   push the artifacts to Maven Central.

See [`gradle-publish-release.yml`][gradle-publish-release.yml] for publishing release versions instructions.

Set the following environment variables in your CI/CD (GitHub Actions, etc.):

```txt
# generated on Sonatype account
PUBLISHING_USERNAME=<username>
PUBLISHING_PASSWORD=<password>

# generated PGP key for signing artifacts
SIGNING_KEY=<PGP key>
SIGNING_PASSWORD=<PGP password>
```

Artifacts are published to Maven Central via Sonatype, using following Gradle task.

```bash
./gradlew -Pversion=<version> -Psign publishAllPublicationsToCentralPortal
```

This command uses `nmcp` Gradle plugin - [link](https://github.com/GradleUp/nmcp).

[gradle-publish-release]: https://github.com/problem4j/problem4j-core/actions/workflows/gradle-publish-release.yml

[gradle-publish-release.yml]: .github/workflows/gradle-publish-release.yml

[maven-central]: https://central.sonatype.com/artifact/io.github.problem4j/problem4j-core
