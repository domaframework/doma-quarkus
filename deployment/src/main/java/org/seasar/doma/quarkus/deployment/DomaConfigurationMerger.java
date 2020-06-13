package org.seasar.doma.quarkus.deployment;

import io.quarkus.agroal.deployment.JdbcDataSourceBuildItem;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.jboss.logging.Logger;
import org.seasar.doma.quarkus.runtime.DomaConfiguration;

public class DomaConfigurationMerger {

  private static final Logger LOGGER = Logger.getLogger(DomaConfigurationMerger.class);

  private final DomaConfiguration configuration;
  private final List<JdbcDataSourceBuildItem> dataSources;

  DomaConfigurationMerger(
      DomaConfiguration configuration, List<JdbcDataSourceBuildItem> dataSources) {
    this.configuration = Objects.requireNonNull(configuration);
    this.dataSources = Objects.requireNonNull(dataSources);
  }

  void merge() {
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
              "The quarkus.doma.datasource-name \""
                  + dataSourceName.get()
                  + "\" is found, but quarkus.datasource named \""
                  + dataSourceName.get()
                  + "\" is not found.");
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

    LOGGER.debugf("configuration: %s", configuration);
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
}
