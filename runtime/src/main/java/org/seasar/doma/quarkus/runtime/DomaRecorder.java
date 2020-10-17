package org.seasar.doma.quarkus.runtime;

import io.quarkus.agroal.DataSource;
import io.quarkus.arc.Arc;
import io.quarkus.arc.runtime.BeanContainerListener;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.annotations.Recorder;
import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.enterprise.inject.Default;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ConfigSupport;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.quarkus.runtime.devmode.HotReplacementScriptFileLoader;
import org.seasar.doma.quarkus.runtime.devmode.HotReplacementSqlFileRepository;

@Recorder
public class DomaRecorder {

  private static volatile List<Path> hotReplacementResourcesDirs;

  public static void setHotReplacementResourcesDirs(List<Path> resourcesDirs) {
    hotReplacementResourcesDirs = Objects.requireNonNull(resourcesDirs);
  }

  public BeanContainerListener configure(DomaSettings domaSettings, LaunchMode launchMode) {
    Objects.requireNonNull(domaSettings);
    Objects.requireNonNull(launchMode);
    return beanContainer -> {
      var producer = beanContainer.instance(DomaProducer.class);

      if (launchMode == LaunchMode.DEVELOPMENT) {
        producer.setSqlFileRepository(
            new HotReplacementSqlFileRepository(hotReplacementResourcesDirs));
        producer.setScriptFileLoader(
            new HotReplacementScriptFileLoader(hotReplacementResourcesDirs));
      } else {
        producer.setSqlFileRepository(domaSettings.sqlFileRepository.create());
        producer.setScriptFileLoader(ConfigSupport.defaultScriptFileLoader);
      }

      producer.setNaming(domaSettings.naming.naming());
      producer.setExceptionSqlLogType(domaSettings.exceptionSqlLogType);
      producer.setNamedSqlLoadScripts(domaSettings.asNamedSqlLoadScripts());
    };
  }

  public Supplier<Config> configureConfig(DomaSettings.DataSourceSettings settings) {
    Objects.requireNonNull(settings);
    return () -> {
      var core = Arc.container().instance(DomaConfig.Core.class).get();
      var dataSourceNameResolver = Arc.container().instance(DataSourceNameResolver.class).get();
      var dataSourceResolver = Arc.container().instance(DataSourceResolver.class).get();
      var candidateName = settings.name;
      return DomaConfig.builder(core)
          .setDataSourceNameSupplier(() -> dataSourceNameResolver.resolve(candidateName))
          .setDataSourceResolver(dataSourceResolver)
          .setDialect(settings.dialect.create())
          .setBatchSize(settings.batchSize)
          .setFetchSize(settings.fetchSize)
          .setMaxRows(settings.maxRows)
          .setQueryTimeout(settings.queryTimeout)
          .build();
    };
  }

  public Supplier<Entityql> configureEntityql(DomaSettings.DataSourceSettings settings) {
    Objects.requireNonNull(settings);
    return () -> {
      var config = resolveConfig(settings);
      return new Entityql(config);
    };
  }

  public Supplier<NativeSql> configureNativeSql(DomaSettings.DataSourceSettings settings) {
    Objects.requireNonNull(settings);
    return () -> {
      var config = resolveConfig(settings);
      return new NativeSql(config);
    };
  }

  private Config resolveConfig(DomaSettings.DataSourceSettings settings) {
    var qualifier = resolveQualifier(settings);
    return Arc.container().instance(Config.class, qualifier).get();
  }

  private Annotation resolveQualifier(DomaSettings.DataSourceSettings settings) {
    if (settings.isDefault) {
      return Default.Literal.INSTANCE;
    }
    return new DataSource.DataSourceLiteral(settings.name);
  }
}
