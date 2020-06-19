package org.seasar.doma.quarkus.runtime;

import io.quarkus.arc.runtime.BeanContainerListener;
import io.quarkus.runtime.annotations.Recorder;
import java.nio.file.Path;
import java.util.List;
import org.seasar.doma.jdbc.ConfigSupport;
import org.seasar.doma.quarkus.runtime.devmode.HotReplacementScriptFileLoader;
import org.seasar.doma.quarkus.runtime.devmode.HotReplacementSqlFileRepository;

@Recorder
public class DomaRecorder {

  private static volatile List<Path> hotReplacementResources;

  public static void setHotReplacementResources(List<Path> resources) {
    hotReplacementResources = resources;
  }

  public BeanContainerListener configure(DomaConfiguration configuration) {
    return beanContainer -> {
      DomaProducer producer = beanContainer.instance(DomaProducer.class);
      producer.setSqlFileRepository(configuration.sqlFileRepository.create());
      producer.setScriptFileLoader(ConfigSupport.defaultScriptFileLoader);
      producer.setDialect(configuration.dialect.get().create());
      producer.setNaming(configuration.naming.naming());
      producer.setExceptionSqlLogType(configuration.exceptionSqlLogType);
      producer.setDataSourceName(configuration.datasourceName.get());
      producer.setBatchSize(configuration.batchSize);
      producer.setFetchSize(configuration.fetchSize);
      producer.setMaxRows(configuration.maxRows);
      producer.setQueryTimeout(configuration.queryTimeout);
      producer.setLogConfiguration(configuration.log);
    };
  }

  public BeanContainerListener configureHotReplacement() {
    return beanContainer -> {
      DomaProducer producer = beanContainer.instance(DomaProducer.class);
      producer.setSqlFileRepository(new HotReplacementSqlFileRepository(hotReplacementResources));
      producer.setScriptFileLoader(new HotReplacementScriptFileLoader(hotReplacementResources));
    };
  }
}
