package org.seasar.doma.quarkus.runtime;

import io.quarkus.arc.DefaultBean;
import io.quarkus.runtime.Startup;
import java.util.Objects;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Commenter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ConfigSupport;
import org.seasar.doma.jdbc.EntityListenerProvider;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.MapKeyNaming;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.QueryImplementors;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.ScriptFileLoader;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.TransactionManager;

@Singleton
public class DomaProducer {

  private volatile SqlFileRepository sqlFileRepository;
  private volatile ScriptFileLoader scriptFileLoader;
  private volatile Dialect dialect;
  private volatile Naming naming;
  private volatile SqlLogType exceptionSqlLogType;
  private volatile String dataSourceName;
  private volatile int batchSize;
  private volatile int fetchSize;
  private volatile int maxRows;
  private volatile int queryTimeout;
  private volatile Optional<InitialScript> initialScript;
  private volatile LogConfiguration logConfiguration;

  public void setSqlFileRepository(SqlFileRepository sqlFileRepository) {
    this.sqlFileRepository = Objects.requireNonNull(sqlFileRepository);
  }

  public void setScriptFileLoader(ScriptFileLoader scriptFileLoader) {
    this.scriptFileLoader = Objects.requireNonNull(scriptFileLoader);
  }

  public void setDialect(Dialect dialect) {
    this.dialect = Objects.requireNonNull(dialect);
  }

  public void setNaming(Naming naming) {
    this.naming = Objects.requireNonNull(naming);
  }

  public void setExceptionSqlLogType(SqlLogType exceptionSqlLogType) {
    this.exceptionSqlLogType = Objects.requireNonNull(exceptionSqlLogType);
  }

  public void setDataSourceName(String dataSourceName) {
    this.dataSourceName = Objects.requireNonNull(dataSourceName);
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public void setFetchSize(int fetchSize) {
    this.fetchSize = fetchSize;
  }

  public void setMaxRows(int maxRows) {
    this.maxRows = maxRows;
  }

  public void setQueryTimeout(int queryTimeout) {
    this.queryTimeout = queryTimeout;
  }

  public void setInitialScript(Optional<InitialScript> initialScript) {
    this.initialScript = Objects.requireNonNull(initialScript);
  }

  public void setLogConfiguration(LogConfiguration logConfiguration) {
    this.logConfiguration = Objects.requireNonNull(logConfiguration);
  }

  @ApplicationScoped
  @DefaultBean
  Dialect dialect() {
    return Objects.requireNonNull(dialect);
  }

  @ApplicationScoped
  @DefaultBean
  SqlFileRepository sqlFileRepository() {
    return Objects.requireNonNull(sqlFileRepository);
  }

  @ApplicationScoped
  @DefaultBean
  ScriptFileLoader scriptFileLoader() {
    return Objects.requireNonNull(scriptFileLoader);
  }

  @ApplicationScoped
  @DefaultBean
  JdbcLogger jdbcLogger() {
    return new JBossJdbcLogger(Objects.requireNonNull(logConfiguration));
  }

  @ApplicationScoped
  @DefaultBean
  RequiresNewController requiresNewController() {
    return ConfigSupport.defaultRequiresNewController;
  }

  @ApplicationScoped
  @DefaultBean
  ClassHelper classHelper() {
    return ConfigSupport.defaultClassHelper;
  }

  @ApplicationScoped
  @DefaultBean
  CommandImplementors commandImplementors() {
    return ConfigSupport.defaultCommandImplementors;
  }

  @ApplicationScoped
  @DefaultBean
  QueryImplementors queryImplementors() {
    return ConfigSupport.defaultQueryImplementors;
  }

  @ApplicationScoped
  @DefaultBean
  UnknownColumnHandler unknownColumnHandler() {
    return ConfigSupport.defaultUnknownColumnHandler;
  }

  @Singleton
  @DefaultBean
  Naming naming() {
    return Objects.requireNonNull(naming);
  }

  @ApplicationScoped
  @DefaultBean
  MapKeyNaming mapKeyNaming() {
    return ConfigSupport.defaultMapKeyNaming;
  }

  @ApplicationScoped
  @DefaultBean
  Commenter commenter() {
    return ConfigSupport.defaultCommenter;
  }

  @ApplicationScoped
  @DefaultBean
  EntityListenerProvider entityListenerProvider() {
    return ConfigSupport.defaultEntityListenerProvider;
  }

  @Singleton
  @DefaultBean
  SqlLogType exceptionSqlLogType() {
    return Objects.requireNonNull(exceptionSqlLogType);
  }

  @ApplicationScoped
  @DefaultBean
  TransactionManager transactionManager() {
    return new UnsupportedTransactionManager();
  }

  @ApplicationScoped
  @DefaultBean
  DbConfig dbConfig(
      DataSource dataSource,
      Dialect dialect,
      SqlFileRepository sqlFileRepository,
      ScriptFileLoader scriptFileLoader,
      JdbcLogger jdbcLogger,
      RequiresNewController requiresNewController,
      ClassHelper classHelper,
      CommandImplementors commandImplementors,
      QueryImplementors queryImplementors,
      SqlLogType exceptionSqlLogType,
      UnknownColumnHandler unknownColumnHandler,
      Naming naming,
      MapKeyNaming mapKeyNaming,
      Commenter commenter,
      EntityListenerProvider entityListenerProvider,
      TransactionManager transactionManager) {
    return new DbConfig(
        dataSource,
        dialect,
        sqlFileRepository,
        scriptFileLoader,
        jdbcLogger,
        requiresNewController,
        classHelper,
        commandImplementors,
        queryImplementors,
        exceptionSqlLogType,
        unknownColumnHandler,
        naming,
        mapKeyNaming,
        commenter,
        entityListenerProvider,
        transactionManager,
        dataSourceName,
        batchSize,
        fetchSize,
        maxRows,
        queryTimeout);
  }

  @ApplicationScoped
  @DefaultBean
  Entityql entityql(Config config) {
    return new Entityql(config);
  }

  @ApplicationScoped
  @DefaultBean
  NativeSql nativeSql(Config config) {
    return new NativeSql(config);
  }

  @Startup
  @ApplicationScoped
  @DefaultBean
  InitialScriptLoader initialScriptLoader(Config config) {
    Objects.requireNonNull(initialScript);
    return new InitialScriptLoader(config, initialScript);
  }
}
