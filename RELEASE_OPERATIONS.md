# Release Operations

## Update README.md

Modify version numbers.

## Run the Maven release:prepare goal

Run the Maven `release:prepare` goal:

```
$ git checkout master
$ git pull
$ ./mvnw --batch-mode -DreleaseVersion=1.0.0 -DdevelopmentVersion=999-SNAPSHOT release:clean release:prepare
```

The value of `releaseVersion` is decided by the draft name of
[Releases](https://github.com/domaframework/doma-quarkus/releases).

## Build and Publish with GitHub Action

(No operation required)

The GitHub Action workflow [Java CI with Gradle](.github/workflows/ci.yml) handles the above push event.

The workflow builds the "doma-quarkus", the "doma-quarkus-deployment",
and the "doma-quarkus-parent" artifacts and publishes them
to the [Maven Central Repository](https://repo1.maven.org/).

After about 30 minutes, each artifact is listed in the following directories:

- https://repo1.maven.org/maven2/org/seasar/doma/doma-quarkus/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-quarkus-deployment/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-quarkus-parent/

## Publish release notes

Open [Releases](https://github.com/domaframework/doma-quarkus/releases)
and publish release notes.

## Announce the release

Announce the release of new version using Twitter.
- [@domaframework](https://twitter.com/domaframework)
