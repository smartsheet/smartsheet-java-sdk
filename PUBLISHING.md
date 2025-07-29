# Publishing to Maven Central

This document explains how to publish the Smartsheet Java SDK to Maven Central using JReleaser.

## Background

The project has migrated from using the Nexus2 publishing plugin to JReleaser for publishing to Maven Central. This change was necessary because Nexus2 has reached end of life.

## Prerequisites

Before publishing, ensure you have:

1. A GPG key for signing artifacts
2. Sonatype OSSRH credentials (username and password)
3. The following environment variables set:
   - `MAVEN_USERNAME`: Your Sonatype OSSRH username
   - `MAVEN_PASSWORD`: Your Sonatype OSSRH password

## Publishing Process

### 1. Validate the JReleaser Configuration

```bash
./gradlew jreleaserConfig
```

This command will validate the JReleaser configuration and report any issues.

### 2. Perform a Dry Run

```bash
./gradlew jreleaserDryRun
```

This command will perform a dry run of the release process without actually publishing anything.

### 3. Publish to Maven Central

```bash
./gradlew publish jreleaserPublish
```

This command will:
1. Build the project
2. Generate the artifacts (JAR, sources JAR, and javadoc JAR)
3. Sign the artifacts
4. Upload the artifacts to Maven Central
5. Close and release the staging repository

## Troubleshooting

### GPG Signing Issues

If you encounter GPG signing issues, ensure that:
- Your GPG key is properly set up
- The GPG agent is running
- You have the correct passphrase for your GPG key

### Maven Central Publishing Issues

If you encounter issues publishing to Maven Central, check:
- Your Sonatype OSSRH credentials are correct
- The environment variables `MAVEN_USERNAME` and `MAVEN_PASSWORD` are properly set
- The project metadata in the POM file is complete and correct

### JReleaser Logs

JReleaser generates detailed logs that can help diagnose issues:

```bash
./gradlew jreleaserPublish --info
```

The logs are also available in the `build/jreleaser/trace.log` file.

## Additional JReleaser Commands

- List all available JReleaser tasks:
  ```bash
  ./gradlew tasks --group=jreleaser
  ```

- Generate a release announcement:
  ```bash
  ./gradlew jreleaserAnnounce
  ```

- Generate a changelog:
  ```bash
  ./gradlew jreleaserChangelog
  ```

## References

- [JReleaser Documentation](https://jreleaser.org/guide/latest/index.html)
- [Maven Central Publishing Requirements](https://central.sonatype.org/publish/requirements/)
- [Gradle Signing Plugin Documentation](https://docs.gradle.org/current/userguide/signing_plugin.html)