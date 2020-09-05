# Release Operations

## Update the release version

Run the Maven `release:prepare` goal:

```
$ git checkout master
$ git pull
$ ./mvnw --batch-mode -DreleaseVersion=1.0.0 -DdevelopmentVersion=999-SNAPSHOT release:clean release:prepare
```

The value of `releaseVersion` is decided by the draft name of
[Releases](https://github.com/domaframework/doma-quarkus/releases).

## Build with GitHub Action

The GitHub Action workflow [Java CI with Maven](.github/workflows/ci.yml) handles the above push event.

The workflow builds the "doma-quarkus-parent", the "doma-quarkus-deployment" and the "doma-quarkus" artifacts
and pushes them to [Sonatype OSSRH](https://central.sonatype.org/pages/ossrh-guide.html).

## Publish artifacts to Maven Central

The Nexus Staging Maven Plugin handles this process.

In a few minutes, all artifacts are copied to the [Maven Central Repository](https://repo1.maven.org/).

## Publish release notes

Open [Releases](https://github.com/domaframework/doma-quarkus/releases)
and publish release notes.

## Update README.md.

Modify version numbers.

## Announce the release

Announce the release of new version using Twitter.
- [@domaframework](https://twitter.com/domaframework)
