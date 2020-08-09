# Release Operations

## Set the release version

Run the Maven `version:set` goal:

```
$ git checkout master
$ git pull
$ ./mvnw versions:set
```

After success, run the Maven `version:commit` goal:

```
$ ./mvnw versions:commit
```

Modify versions in README.md.

Commit the release version:

```
$ git commit -m "Release v1.0.0"
```

## Push to the master branch

Create a tag and push the commit and the tag:

```
$ git tag -a v1.0.0 -m "v1.0.0"
$ git push origin master --tags
```

## Build with GitHub Action

The GitHub Action workflow [Java CI with Maven](.github/workflows/ci.yml) handles the above push event.

The workflow builds the "doma-quarkus-parent", the "doma-quarkus-deployment" and the "doma-quarkus" artifacts
and pushes them to Maven Central[Sonatype OSSRH](https://central.sonatype.org/pages/ossrh-guide.html).

## Publish artifacts to Maven Central from Sonatype OSSRH

Follow the instructions below:

- Open [Nexus Repository Manager](https://oss.sonatype.org/).
- Log in to the manager.
- Select "Staging Repositories" from the side menu.
- Check the repository of Doma.
- Push the "Release" button.

In a few minutes, all artifacts are copied to the [Maven Central Repository](https://repo1.maven.org/).

## Publish release notes

Open [Releases](https://github.com/domaframework/doma-quarkus/releases)
and publish release notes.

## Announce the release

Announce the release of new version using Twitter.
- [@domaframework](https://twitter.com/domaframework)

## Set the next version

Run the Maven `version:set` goal:

```
$ ./mvnw versions:set
```

After success, run the Maven `version:commit` goal:

```
$ ./mvnw versions:commit
```

Commit and push the next version:

```
$ git commit -m "Next version [skip ci]"
$ git push origin master
```
