package org.seasar.doma.quarkus.runtime;

import io.quarkus.arc.runtime.BeanContainerListener;
import io.quarkus.runtime.annotations.Recorder;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.ConfigSupport;
import org.seasar.doma.quarkus.runtime.devmode.HotReplacementScriptFileLoader;
import org.seasar.doma.quarkus.runtime.devmode.HotReplacementSqlFileRepository;

@Recorder
public class DomaRecorder {

  private static volatile List<Path> hotReplacementResourcesDirs;

  public static void setHotReplacementResourcesDirs(List<Path> resourcesDirs) {
    hotReplacementResourcesDirs = Objects.requireNonNull(resourcesDirs);
  }

  public BeanContainerListener configure(DomaConfiguration configuration, boolean isDevMode) {
    return beanContainer -> {
      DomaProducer producer = beanContainer.instance(DomaProducer.class);
      if (isDevMode) {
        producer.setSqlFileRepository(
            new HotReplacementSqlFileRepository(hotReplacementResourcesDirs));
        producer.setScriptFileLoader(
            new HotReplacementScriptFileLoader(hotReplacementResourcesDirs));
      } else {
        producer.setSqlFileRepository(configuration.sqlFileRepository.create());
        producer.setScriptFileLoader(ConfigSupport.defaultScriptFileLoader);
      }
      producer.setDialect(configuration.dialect.get().create());
      producer.setNaming(configuration.naming.naming());
      producer.setExceptionSqlLogType(configuration.exceptionSqlLogType);
      producer.setDataSourceName(configuration.datasourceName.get());
      producer.setBatchSize(configuration.batchSize);
      producer.setFetchSize(configuration.fetchSize);
      producer.setMaxRows(configuration.maxRows);
      producer.setQueryTimeout(configuration.queryTimeout);
      producer.setInitialScript(configuration.sqlLoadScript.map(InitialScript::new));
      producer.setLogConfiguration(configuration.log);
    };
  }
}
