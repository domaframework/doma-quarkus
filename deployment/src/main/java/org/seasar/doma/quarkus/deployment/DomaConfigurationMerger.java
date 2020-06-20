package org.seasar.doma.quarkus.deployment;

import io.quarkus.agroal.deployment.JdbcDataSourceBuildItem;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.runtime.LaunchMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.jboss.logging.Logger;
import org.seasar.doma.quarkus.runtime.DomaConfiguration;
import org.seasar.doma.quarkus.runtime.InitialScript;

public class DomaConfigurationMerger {

  private static final Logger LOGGER = Logger.getLogger(DomaConfigurationMerger.class);

  private final DomaConfiguration configuration;
  private final List<JdbcDataSourceBuildItem> dataSources;
  private final ApplicationArchivesBuildItem applicationArchives;
  private final LaunchModeBuildItem launchMode;

  DomaConfigurationMerger(
      DomaConfiguration configuration,
      List<JdbcDataSourceBuildItem> dataSources,
      ApplicationArchivesBuildItem applicationArchives,
      LaunchModeBuildItem launchMode) {
    this.configuration = Objects.requireNonNull(configuration);
    this.dataSources = Objects.requireNonNull(dataSources);
    this.applicationArchives = Objects.requireNonNull(applicationArchives);
    this.launchMode = Objects.requireNonNull(launchMode);
  }

  void merge() {
    mergeDataSourceDependentItems();
    mergeSqlLoadScript();
    LOGGER.debugf("configuration: %s", configuration);
  }

  private void mergeDataSourceDependentItems() {
    Optional<String> dataSourceName = configuration.datasourceName;
    Optional<DomaConfiguration.DialectType> dialect = configuration.dialect;

    if (dataSourceName.isPresent()) {
      if (dialect.isPresent()) {
        // do nothing
      } else {
        Optional<JdbcDataSourceBuildItem> dataSource =
            dataSources.stream()
                .filter(it -> dataSourceName.get().equals(it.getName()))
                .findFirst();
        if (dataSource.isPresent()) {
          String dbKind = dataSource.get().getDbKind();
          configuration.dialect = Optional.of(inferDialectType(dbKind));
        } else {
          throw new IllegalStateException(
              String.format(
                  "'quarkus.doma.datasource-name=%s' is found, "
                      + "but 'quarkus.datasource.\"datasource-name\"' is not found.",
                  dataSourceName.get()));
        }
      }
    } else {
      Optional<JdbcDataSourceBuildItem> dataSource =
          Stream.concat(
                  dataSources.stream().filter(JdbcDataSourceBuildItem::isDefault),
                  dataSources.stream())
              .findFirst();
      if (dataSource.isPresent()) {
        configuration.datasourceName = Optional.of(dataSource.get().getName());
        if (dialect.isPresent()) {
          // do nothing
        } else {
          String dbKind = dataSource.get().getDbKind();
          configuration.dialect = Optional.of(inferDialectType(dbKind));
        }
      } else {
        throw new IllegalStateException(
            "The quarkus.datasource is not found. "
                + "Specify the configuration in your application.properties.");
      }
    }
  }

  private DomaConfiguration.DialectType inferDialectType(String dbKind) {
    switch (dbKind) {
      case "h2":
        return DomaConfiguration.DialectType.H2;
      case "mssql":
        return DomaConfiguration.DialectType.MSSQL;
      case "mysql":
      case "mariadb":
        return DomaConfiguration.DialectType.MYSQL;
      case "postgresql":
      case "pgsql":
      case "pg":
        return DomaConfiguration.DialectType.POSTGRES;
      default:
        throw new IllegalStateException(
            "Can't infer the dialect from the dbKind \""
                + dbKind
                + "\". The dbKind is illegal or not supported.");
    }
  }

  private void mergeSqlLoadScript() {
    Optional<String> sqlLoadScript = configuration.sqlLoadScript;
    if (sqlLoadScript.isPresent()) {
      if (sqlLoadScript.get().equals(InitialScript.NO_FILE)) {
        configuration.sqlLoadScript = Optional.empty();
      } else {
        Path path = applicationArchives.getRootArchive().getChildPath(sqlLoadScript.get());
        if (path == null || Files.isDirectory(path)) {
          throw new IllegalStateException(
              String.format(
                  "Can't find the file referenced in 'quarkus.doma.sql-load-script=%s'. "
                      + "Remove property or add file to your path.",
                  sqlLoadScript.get()));
        }
      }
    } else {
      LaunchMode mode = launchMode.getLaunchMode();
      if (mode == LaunchMode.DEVELOPMENT || mode == LaunchMode.TEST) {
        Path path = applicationArchives.getRootArchive().getChildPath(InitialScript.DEFAULT);
        if (path == null || Files.isDirectory(path)) {
          configuration.sqlLoadScript = Optional.empty();
        } else {
          configuration.sqlLoadScript = Optional.of(InitialScript.DEFAULT);
        }
      } else {
        configuration.sqlLoadScript = Optional.empty();
      }
    }
  }
}
