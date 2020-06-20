Quarkus Extension for Doma
==========================

## Introduction

Quarkus Extension for Doma provides the following features:

- Hot reload
- Automatic bean register
- Configuration
- Support for native images

### Hot reload

In development mode, SQL and Script files are hot reloaded.

### Automatic bean register

Our extension registers all DAO beans to the Quarkus CDI container automatically.

### Configuration

You can write the following configurations in your application.properties file: 

```
quarkus.doma.datasource-name=default
quarkus.doma.dialect=h2
quarkus.doma.sql-file-repository=greedy-cache
quarkus.doma.naming=none
quarkus.doma.exception-sql-log-type=none
quarkus.doma.batch-size=0
quarkus.doma.fetch-size=0
quarkus.doma.max-rows=0
quarkus.doma.query-timeout=0

quarkus.doma.log.sql=false
quarkus.doma.log.dao=false
quarkus.doma.log.closing-failure=false
```

The above properties are all optional.

See [quarkus-doma.adoc](./quarkus-doma.adoc) for more details.

### Support for native images

Our extension recognizes reflective classes and resources,
and includes them into your native image without additional configurations.

## How to use

### Gradle

```groovy
dependencies {
    annotationProcessor "org.seasar.doma:doma-processor:2.37.0"
    implementation "org.seasar.doma:doma-core:2.37.0"
    implementation "org.seasar.doma:doma-quarkus-deployment:0.2.0"
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>org.seasar.doma</groupId>
        <artifactId>doma-processor</artifactId>
        <version>2.37.0</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.seasar.doma</groupId>
        <artifactId>doma-core</artifactId>
        <version>2.37.0</version>
    </dependency>
    <dependency>
        <groupId>org.seasar.doma</groupId>
        <artifactId>doma-quarkus-deployment</artifactId>
        <version>0.2.0</version>
    </dependency>
    ...
</dependencies>
```

## Sample project
- [quarkus-sample](https://github.com/domaframework/quarkus-sample)

## Related information
- [Building Applications with Gradle](https://quarkus.io/guides/gradle-tooling)
- [Building Applications with Maven](https://quarkus.io/guides/maven-tooling)
