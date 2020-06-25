package org.seasar.doma.quarkus.runtime;

import io.quarkus.arc.runtime.BeanContainerListener;
import io.quarkus.runtime.LaunchMode;
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

  public BeanContainerListener configure(DomaSettings domaSettings, LaunchMode launchMode) {
    return beanContainer -> {
      DomaProducer producer = beanContainer.instance(DomaProducer.class);
      if (launchMode == LaunchMode.DEVELOPMENT) {
        producer.setSqlFileRepository(
            new HotReplacementSqlFileRepository(hotReplacementResourcesDirs));
        producer.setScriptFileLoader(
            new HotReplacementScriptFileLoader(hotReplacementResourcesDirs));
      } else {
        producer.setSqlFileRepository(domaSettings.sqlFileRepository.create());
        producer.setScriptFileLoader(ConfigSupport.defaultScriptFileLoader);
      }
      producer.setDialect(domaSettings.dialect.create());
      producer.setNaming(domaSettings.naming.naming());
      producer.setExceptionSqlLogType(domaSettings.exceptionSqlLogType);
      producer.setDataSourceName(domaSettings.dataSourceName);
      producer.setBatchSize(domaSettings.batchSize);
      producer.setFetchSize(domaSettings.fetchSize);
      producer.setMaxRows(domaSettings.maxRows);
      producer.setQueryTimeout(domaSettings.queryTimeout);
      producer.setSqlLoadScript(domaSettings.sqlLoadScript);
      producer.setLogSettings(domaSettings.log);
    };
  }
}
