# Contributing guide

## Legal

All original contributions to Doma are licensed under
the ASL - [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0) or later.

## Reporting an issue

This project uses GitHub issues to manage the issues. Open an issue directly in GitHub.

Write the issue in English to share it with many people.

## Before you contribute

To contribute, use GitHub Pull Requests, from your own fork.

## Setup

- Install Git and configure your GitHub access
- Install JDK 11
  - We recommend that you use [SDKMAN](https://sdkman.io/jdks) to get JDKs
- Install GraalVM if you build a native executable

### Build

Clone the repository and navigate to the root directory.
Then run the Gradle `build` task:

```
$ git clone https://github.com/domaframework/doma-quarkus.git
$ cd doma-quarkus
$ ./mvnw install
```

### IDE

#### IntelliJ IEDA

Import the root directory as a Maven project.

### Code Style

We use [spotless](https://github.com/diffplug/spotless) and
[google-java-format](https://github.com/google/google-java-format) 1.7 for code formatting.

To format, just run Maven as follows:

```
$ ./mvnw spotless:apply
```

To run google-java-format in your IDE,
see https://github.com/google/google-java-format#using-the-formatter.

