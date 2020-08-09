Quarkus Extension for Doma
==========================

[![Build Status](https://github.com/domaframework/doma-quarkus/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/domaframework/doma-quarkus/actions?query=workflow%3A%22Java+CI+with+Maven%22)

## Introduction

Quarkus Extension for Doma provides the following features:

- Hot reloading
- Automatic bean register
- Automatic SQL execution on startup
- Configuration
- Multiple Datasources
- Support for native images

### Hot reloading

In development mode, SQL and Script files are hot reloaded.

### Automatic bean register

Our extension registers all DAO beans to the Quarkus CDI container automatically.

### Automatic SQL import on startup

Our extension executes ``import.sql`` automatically when Doma starts.

### Configuration

You can write the following configurations in your application.properties file: 

```
quarkus.doma.sql-file-repository=greedy-cache
quarkus.doma.naming=none
quarkus.doma.exception-sql-log-type=none
quarkus.doma.dialect=h2
quarkus.doma.batch-size=10
quarkus.doma.fetch-size=50
quarkus.doma.max-rows=500
quarkus.doma.query-timeout=5000
quarkus.doma.sql-load-script=import.sql
quarkus.doma.log.sql=false
quarkus.doma.log.dao=false
quarkus.doma.log.closing-failure=false
```

The above properties are all optional.

See [quarkus-doma.adoc](./quarkus-doma.adoc) for more details.

### Multiple Datasources

You can bind Doma's configurations to each datasource as follows:

```
# default datasource
quarkus.datasource.db-kind=h2
quarkus.datasource.username=username-default
quarkus.datasource.jdbc.url=jdbc:h2:tcp://localhost/mem:default
quarkus.datasource.jdbc.min-size=3
quarkus.datasource.jdbc.max-size=13

# inventory datasource
quarkus.datasource.inventory.db-kind=h2
quarkus.datasource.inventory.username=username2
quarkus.datasource.inventory.jdbc.url=jdbc:h2:tcp://localhost/mem:inventory
quarkus.datasource.inventory.jdbc.min-size=2
quarkus.datasource.inventory.jdbc.max-size=12

# Doma's configuration bound to the above default datasource
quarkus.doma.dialect=h2
quarkus.doma.batch-size=10
quarkus.doma.fetch-size=50
quarkus.doma.max-rows=500
quarkus.doma.query-timeout=5000
quarkus.doma.sql-load-script=import.sql

# Doma's configuration bound to the above inventory datasource
quarkus.doma.inventory.dialect=h2
quarkus.doma.inventory.batch-size=10
quarkus.doma.inventory.fetch-size=50
quarkus.doma.inventory.max-rows=500
quarkus.doma.inventory.query-timeout=5000
quarkus.doma.inventory.sql-load-script=import.sql
```

You can inject the named Doma's resource 
using the `io.quarkus.agroal.DataSource` qualifier as follows:

```java
@Inejct
Config defaultConfig;

@Inejct
Entityql defaultEntityql;

@Inejct
Nativeql defaultNativeql;

@Inejct
@DataSource("inventory")
Config invetoryConfig;

@Inejct
@DataSource("inventory")
Entityql inventoryEntityql;

@Inejct
@DataSource("inventory")
Nativeql inventoryNativeql;
```

### Support for native images

Our extension recognizes reflective classes and resources,
and includes them into your native image without additional configurations.

## How to use

### Gradle

```groovy
dependencies {
    annotationProcessor "org.seasar.doma:doma-processor:2.40.0"
    implementation "org.seasar.doma:doma-core:2.40.0"
    implementation "org.seasar.doma:doma-quarkus-deployment:1.0.0"
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>org.seasar.doma</groupId>
        <artifactId>doma-processor</artifactId>
        <version>2.40.0</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.seasar.doma</groupId>
        <artifactId>doma-core</artifactId>
        <version>2.40.0</version>
    </dependency>
    <dependency>
        <groupId>org.seasar.doma</groupId>
        <artifactId>doma-quarkus-deployment</artifactId>
        <version>1.0.0</version>
    </dependency>
    ...
</dependencies>
```

## Sample project
- [quarkus-sample](https://github.com/domaframework/quarkus-sample)

## Related information
- [Building Applications with Gradle](https://quarkus.io/guides/gradle-tooling)
- [Building Applications with Maven](https://quarkus.io/guides/maven-tooling)
