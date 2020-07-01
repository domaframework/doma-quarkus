package org.seasar.doma.quarkus.runtime;

import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.Unremovable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.inject.Singleton;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Commenter;
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
import org.seasar.doma.jdbc.tx.TransactionManager;

@Singleton
public class DomaProducer {

  private volatile SqlFileRepository sqlFileRepository;
  private volatile ScriptFileLoader scriptFileLoader;
  private volatile Naming naming;
  private volatile SqlLogType exceptionSqlLogType;
  private volatile Map<String, String> namedSqlLoadScripts;
  private volatile LogPreferences logPreferences;

  public void setSqlFileRepository(SqlFileRepository sqlFileRepository) {
    this.sqlFileRepository = Objects.requireNonNull(sqlFileRepository);
  }

  public void setScriptFileLoader(ScriptFileLoader scriptFileLoader) {
    this.scriptFileLoader = Objects.requireNonNull(scriptFileLoader);
  }

  public void setNaming(Naming naming) {
    this.naming = Objects.requireNonNull(naming);
  }

  public void setExceptionSqlLogType(SqlLogType exceptionSqlLogType) {
    this.exceptionSqlLogType = Objects.requireNonNull(exceptionSqlLogType);
  }

  public void setNamedSqlLoadScripts(Map<String, String> namedSqlLoadScripts) {
    Objects.requireNonNull(namedSqlLoadScripts);
    this.namedSqlLoadScripts = Collections.unmodifiableMap(namedSqlLoadScripts);
  }

  public void setLogPreferences(LogPreferences logPreferences) {
    this.logPreferences = Objects.requireNonNull(logPreferences);
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
  DomaLogger jdbcLogger(LogPreferences logPreferences) {
    return new DomaLogger(logPreferences);
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

  @Singleton
  @Named("doma.namedSqlLoadScripts")
  public Map<String, String> getNamedSqlLoadScripts() {
    return namedSqlLoadScripts;
  }

  @Singleton
  @DefaultBean
  LogPreferences logPreferences() {
    return Objects.requireNonNull(logPreferences);
  }

  @ApplicationScoped
  @DefaultBean
  @Unremovable
  DomaConfig.Core core(
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
    return new DomaConfig.Core(
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
        transactionManager);
  }
}
