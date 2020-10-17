package org.seasar.doma.quarkus.deployment;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import io.quarkus.agroal.spi.JdbcDataSourceBuildItem;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.jboss.logging.Logger;
import org.seasar.doma.quarkus.runtime.DomaSettings;

public class DomaSettingsFactory {

  private static final Logger logger = Logger.getLogger(DomaSettingsFactory.class);

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
    this.dataSources = new ArrayList<>(Objects.requireNonNull(dataSources));
    this.applicationArchives = Objects.requireNonNull(applicationArchives);
    this.launchMode = Objects.requireNonNull(launchMode);
  }

  DomaSettings create() {
    var settings = new DomaSettings();
    settings.sqlFileRepository = buildTimeConfig.sqlFileRepository;
    settings.naming = buildTimeConfig.naming;
    settings.exceptionSqlLogType = buildTimeConfig.exceptionSqlLogType;
    settings.dataSources = dataSources();
    if (dataSources.isEmpty()) {
      throw new IllegalStateException("The quarkus.datasource is empty. Specify it.");
    }
    logger.debugf("settings: %s", settings);
    return settings;
  }

  private List<DomaSettings.DataSourceSettings> dataSources() {
    var namedDataSources =
        dataSources.stream()
            .collect(
                toMap(
                    JdbcDataSourceBuildItem::getName,
                    Function.identity(),
                    (a, b) -> a,
                    LinkedHashMap::new));
    return namedDataSources.entrySet().stream()
        .map(
            e -> {
              var name = e.getKey();
              var dataSource = e.getValue();
              DomaBuildTimeConfig.DataSourceBuildTimeConfig dataSourceBuildTimeConfig;
              if (dataSource.isDefault()) {
                dataSourceBuildTimeConfig = buildTimeConfig.defaultDataSource;
              } else {
                dataSourceBuildTimeConfig = buildTimeConfig.namedDataSources.get(name);
                if (dataSourceBuildTimeConfig == null) {
                  dataSourceBuildTimeConfig = new DomaBuildTimeConfig.DataSourceBuildTimeConfig();
                }
              }
              return createDataSourceSettings(dataSource, dataSourceBuildTimeConfig);
            })
        .collect(toList());
  }

  private DomaSettings.DataSourceSettings createDataSourceSettings(
      JdbcDataSourceBuildItem dataSource,
      DomaBuildTimeConfig.DataSourceBuildTimeConfig dataSourceBuildTimeConfig) {
    var settings = new DomaSettings.DataSourceSettings();
    settings.name = dataSource.getName();
    settings.isDefault = dataSource.isDefault();
    settings.dialect =
        dataSourceBuildTimeConfig.dialect.orElseGet(
            () -> resolveDialectType(dataSource.getDbKind()));
    settings.batchSize = dataSourceBuildTimeConfig.batchSize;
    settings.fetchSize = dataSourceBuildTimeConfig.fetchSize;
    settings.maxRows = dataSourceBuildTimeConfig.maxRows;
    settings.queryTimeout = dataSourceBuildTimeConfig.queryTimeout;
    settings.sqlLoadScript = resolveSqlLoadScript(dataSourceBuildTimeConfig.sqlLoadScript);
    return settings;
  }

  private DomaSettings.DialectType resolveDialectType(String dbKind) {
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

  private String resolveSqlLoadScript(Optional<String> sqlLoadScript) {
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
}
