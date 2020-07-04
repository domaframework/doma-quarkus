package org.seasar.doma.quarkus.runtime;

import io.quarkus.agroal.runtime.DataSources;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.Unremovable;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

public interface DataSourceResolver {

  DataSource resolve(String dataSourceName);

  @Singleton
  @DefaultBean
  @Unremovable
  class DefaultDataSourceResolver implements DataSourceResolver {

    private final DataSources dataSources;

    @Inject
    public DefaultDataSourceResolver(DataSources dataSources) {
      this.dataSources = Objects.requireNonNull(dataSources);
    }

    @Override
    public DataSource resolve(String dataSourceName) {
      Objects.requireNonNull(dataSourceName);
      return dataSources.getDataSource(dataSourceName);
    }
  }
}
