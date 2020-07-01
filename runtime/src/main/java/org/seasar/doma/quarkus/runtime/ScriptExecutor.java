package org.seasar.doma.quarkus.runtime;

import io.quarkus.agroal.runtime.DataSources;
import io.quarkus.arc.DefaultBean;
import io.quarkus.runtime.StartupEvent;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import org.jboss.logging.Logger;
import org.seasar.doma.internal.util.ResourceUtil;

@ApplicationScoped
@DefaultBean
public class ScriptExecutor {

  private static Logger LOGGER = Logger.getLogger(ScriptExecutor.class.getName());

  private final Map<String, String> namedSqlLoadScripts;

  @Inject
  public ScriptExecutor(
      @Named("doma.namedSqlLoadScripts") Map<String, String> namedSqlLoadScripts) {
    Objects.requireNonNull(namedSqlLoadScripts);
    this.namedSqlLoadScripts = Collections.unmodifiableMap(namedSqlLoadScripts);
  }

  void onStartup(@Observes StartupEvent event) throws Exception {
    for (var entry : namedSqlLoadScripts.entrySet()) {
      var name = entry.getKey();
      var path = entry.getValue();
      var dataSource = DataSources.fromName(name);
      if (dataSource == null) {
        throw new IllegalStateException(String.format("The datasource '%s' is not found.", name));
      }
      execute(dataSource, path);
    }
  }

  private void execute(DataSource dataSource, String path) throws Exception {
    LOGGER.infof("Execute %s", path);
    var sql = ResourceUtil.getResourceAsString(path);
    try (var connection = dataSource.getConnection()) {
      try (var statement = connection.createStatement()) {
        statement.execute(sql);
      }
    }
  }
}
