package org.seasar.doma.quarkus.deployment;

import io.quarkus.agroal.deployment.JdbcDataSourceBuildItem;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jboss.logging.Logger;
import org.seasar.doma.quarkus.runtime.DomaSettings;

public class DomaSettingsFactory {

  private static final Logger LOGGER = Logger.getLogger(DomaSettingsFactory.class);

  private final DomaBuildTimeConfig buildTimeConfig;
  private final List<JdbcDataSourceBuildItem> dataSources;
  private final ApplicationArchivesBuildItem applicationArchives;
  private final LaunchModeBuildItem launchMode;

  DomaSettingsFactory(
      DomaBuildTimeConfig buildTimeConfig,
      List<JdbcDataSourceBuildItem> dataSources,
      ApplicationArchivesBuildItem applicationArchives,
      LaunchModeBuildItem launchMode) {
    this.buildTimeConfig = Objects.requireNonNull(buildTimeConfig);
    this.dataSources = Objects.requireNonNull(dataSources);
    this.applicationArchives = Objects.requireNonNull(applicationArchives);
    this.launchMode = Objects.requireNonNull(launchMode);
  }

  DomaSettings create() {
    var settings = new DomaSettings();
    var items = dataSourceDependentItems();
    settings.dataSourceName = items.dataSourceName;
    settings.dialect = items.dialect;
    settings.sqlFileRepository = buildTimeConfig.sqlFileRepository;
    settings.naming = buildTimeConfig.naming;
    settings.exceptionSqlLogType = buildTimeConfig.exceptionSqlLogType;
    settings.sqlLoadScript = sqlLoadScript();
    settings.batchSize = buildTimeConfig.batchSize;
    settings.fetchSize = buildTimeConfig.fetchSize;
    settings.maxRows = buildTimeConfig.maxRows;
    settings.queryTimeout = buildTimeConfig.queryTimeout;
    settings.log = log();
    LOGGER.debugf("settings: %s", settings);
    return settings;
  }

  private DataSourceDependentItems dataSourceDependentItems() {
    var items = new DataSourceDependentItems();
    var dataSourceName = buildTimeConfig.datasourceName;
    var dialect = buildTimeConfig.dialect;

    if (dataSourceName.isPresent()) {
      items.dataSourceName = dataSourceName.get();
      if (dialect.isPresent()) {
        items.dialect = dialect.get();
      } else {
        var dataSource =
            dataSources.stream()
                .filter(it -> dataSourceName.get().equals(it.getName()))
                .findFirst();
        if (dataSource.isPresent()) {
          var dbKind = dataSource.get().getDbKind();
          items.dialect = inferDialectType(dbKind);
        } else {
          throw new IllegalStateException(
              String.format(
                  "'quarkus.doma.datasource-name=%s' is found, "
                      + "but 'quarkus.datasource.\"datasource-name\"' is not found.",
                  dataSourceName.get()));
        }
      }
    } else {
      var dataSource =
          Stream.concat(
                  dataSources.stream().filter(JdbcDataSourceBuildItem::isDefault),
                  dataSources.stream())
              .findFirst();
      if (dataSource.isPresent()) {
        items.dataSourceName = dataSource.get().getName();
        if (dialect.isPresent()) {
          items.dialect = dialect.get();
        } else {
          var dbKind = dataSource.get().getDbKind();
          items.dialect = inferDialectType(dbKind);
        }
      } else {
        throw new IllegalStateException(
            "The quarkus.datasource is not found. "
                + "Specify the configuration in your application.properties.");
      }
    }
    return items;
  }

  private DomaSettings.DialectType inferDialectType(String dbKind) {
    switch (dbKind) {
      case "h2":
        return DomaSettings.DialectType.H2;
      case "mssql":
        return DomaSettings.DialectType.MSSQL;
      case "mysql":
      case "mariadb":
        return DomaSettings.DialectType.MYSQL;
      case "postgresql":
      case "pgsql":
      case "pg":
        return DomaSettings.DialectType.POSTGRES;
      default:
        throw new IllegalStateException(
            "Can't infer the dialect from the dbKind \""
                + dbKind
                + "\". The dbKind is illegal or not supported.");
    }
  }

  private String sqlLoadScript() {
    var sqlLoadScript = buildTimeConfig.sqlLoadScript;
    if (sqlLoadScript.isPresent()) {
      if (sqlLoadScript.get().equals(DomaBuildTimeConfig.SQL_LOAD_SCRIPT_NO_FILE)) {
        return null;
      } else {
        var path = applicationArchives.getRootArchive().getChildPath(sqlLoadScript.get());
        if (path == null || Files.isDirectory(path)) {
          throw new IllegalStateException(
              String.format(
                  "Can't find the file referenced in 'quarkus.doma.sql-load-script=%s'. "
                      + "Remove property or add file to your path.",
                  sqlLoadScript.get()));
        }
        return sqlLoadScript.get();
      }
    } else {
      if (launchMode.getLaunchMode().isDevOrTest()) {
        var path =
            applicationArchives
                .getRootArchive()
                .getChildPath(DomaBuildTimeConfig.SQL_LOAD_SCRIPT_DEFAULT);
        if (path == null || Files.isDirectory(path)) {
          return null;
        } else {
          return DomaBuildTimeConfig.SQL_LOAD_SCRIPT_DEFAULT;
        }
      } else {
        return null;
      }
    }
  }

  private DomaSettings.LogSettings log() {
    var log = buildTimeConfig.log;
    return new DomaSettings.LogSettings(log.sql, log.dao, log.closingFailure);
  }

  static class DataSourceDependentItems {
    String dataSourceName;
    DomaSettings.DialectType dialect;
  }
}
