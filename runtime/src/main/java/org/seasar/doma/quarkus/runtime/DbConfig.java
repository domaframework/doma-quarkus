package org.seasar.doma.quarkus.runtime;

import java.util.Objects;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Commenter;
import org.seasar.doma.jdbc.Config;
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
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class DbConfig implements Config {

  private final DataSource dataSource;
  private final Dialect dialect;
  private final SqlFileRepository sqlFileRepository;
  private final ScriptFileLoader scriptFileLoader;
  private final JdbcLogger jdbcLogger;
  private final RequiresNewController requiresNewController;
  private final ClassHelper classHelper;
  private final CommandImplementors commandImplementors;
  private final QueryImplementors queryImplementors;
  private final SqlLogType exceptionSqlLogType;
  private final UnknownColumnHandler unknownColumnHandler;
  private final Naming naming;
  private final MapKeyNaming mapKeyNaming;
  private final Commenter commenter;
  private final EntityListenerProvider entityListenerProvider;
  private final TransactionManager transactionManager;
  private final String dataSourceName;
  private final int batchSize;
  private final int fetchSize;
  private final int maxRows;
  private final int queryTimeout;

  public DbConfig(
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
      TransactionManager transactionManager,
      String dataSourceName,
      int batchSize,
      int fetchSize,
      int maxRows,
      int queryTimeout) {
    this.dataSource = Objects.requireNonNull(dataSource);
    this.dialect = Objects.requireNonNull(dialect);
    this.sqlFileRepository = Objects.requireNonNull(sqlFileRepository);
    this.scriptFileLoader = Objects.requireNonNull(scriptFileLoader);
    this.jdbcLogger = Objects.requireNonNull(jdbcLogger);
    this.requiresNewController = Objects.requireNonNull(requiresNewController);
    this.classHelper = Objects.requireNonNull(classHelper);
    this.commandImplementors = Objects.requireNonNull(commandImplementors);
    this.queryImplementors = Objects.requireNonNull(queryImplementors);
    this.exceptionSqlLogType = Objects.requireNonNull(exceptionSqlLogType);
    this.unknownColumnHandler = Objects.requireNonNull(unknownColumnHandler);
    this.naming = Objects.requireNonNull(naming);
    this.mapKeyNaming = Objects.requireNonNull(mapKeyNaming);
    this.commenter = Objects.requireNonNull(commenter);
    this.entityListenerProvider = Objects.requireNonNull(entityListenerProvider);
    this.transactionManager = transactionManager;
    this.dataSourceName = Objects.requireNonNull(dataSourceName);
    this.batchSize = batchSize;
    this.fetchSize = fetchSize;
    this.maxRows = maxRows;
    this.queryTimeout = queryTimeout;
  }

  @Override
  public Dialect getDialect() {
    return dialect;
  }

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public SqlFileRepository getSqlFileRepository() {
    return sqlFileRepository;
  }

  @Override
  public ScriptFileLoader getScriptFileLoader() {
    return scriptFileLoader;
  }

  @Override
  public JdbcLogger getJdbcLogger() {
    return jdbcLogger;
  }

  @Override
  public RequiresNewController getRequiresNewController() {
    return requiresNewController;
  }

  @Override
  public ClassHelper getClassHelper() {
    return classHelper;
  }

  @Override
  public CommandImplementors getCommandImplementors() {
    return commandImplementors;
  }

  @Override
  public QueryImplementors getQueryImplementors() {
    return queryImplementors;
  }

  @Override
  public SqlLogType getExceptionSqlLogType() {
    return exceptionSqlLogType;
  }

  @Override
  public UnknownColumnHandler getUnknownColumnHandler() {
    return unknownColumnHandler;
  }

  @Override
  public Naming getNaming() {
    return naming;
  }

  @Override
  public MapKeyNaming getMapKeyNaming() {
    return mapKeyNaming;
  }

  @Override
  public TransactionManager getTransactionManager() {
    return transactionManager;
  }

  @Override
  public Commenter getCommenter() {
    return commenter;
  }

  @Override
  public EntityListenerProvider getEntityListenerProvider() {
    return entityListenerProvider;
  }

  @Override
  public String getDataSourceName() {
    return dataSourceName;
  }

  @Override
  public int getMaxRows() {
    return maxRows;
  }

  @Override
  public int getFetchSize() {
    return fetchSize;
  }

  @Override
  public int getBatchSize() {
    return batchSize;
  }

  @Override
  public int getQueryTimeout() {
    return queryTimeout;
  }

  public Builder builder() {
    Builder builder = new Builder();
    builder.dataSource = this.dataSource;
    builder.dialect = this.dialect;
    builder.sqlFileRepository = this.sqlFileRepository;
    builder.scriptFileLoader = this.scriptFileLoader;
    builder.jdbcLogger = this.jdbcLogger;
    builder.requiresNewController = this.requiresNewController;
    builder.classHelper = this.classHelper;
    builder.commandImplementors = this.commandImplementors;
    builder.queryImplementors = this.queryImplementors;
    builder.exceptionSqlLogType = this.exceptionSqlLogType;
    builder.unknownColumnHandler = this.unknownColumnHandler;
    builder.naming = this.naming;
    builder.mapKeyNaming = this.mapKeyNaming;
    builder.commenter = this.commenter;
    builder.entityListenerProvider = this.entityListenerProvider;
    builder.transactionManager = this.transactionManager;
    builder.dataSourceName = this.dataSourceName;
    builder.batchSize = this.batchSize;
    builder.fetchSize = this.fetchSize;
    builder.maxRows = this.maxRows;
    builder.queryTimeout = this.queryTimeout;
    return builder;
  }

  public static class Builder {

    private DataSource dataSource;
    private Dialect dialect;
    private SqlFileRepository sqlFileRepository;
    private ScriptFileLoader scriptFileLoader;
    private JdbcLogger jdbcLogger;
    private RequiresNewController requiresNewController;
    private ClassHelper classHelper;
    private CommandImplementors commandImplementors;
    private QueryImplementors queryImplementors;
    private SqlLogType exceptionSqlLogType;
    private UnknownColumnHandler unknownColumnHandler;
    private Naming naming;
    private MapKeyNaming mapKeyNaming;
    private Commenter commenter;
    private EntityListenerProvider entityListenerProvider;
    private TransactionManager transactionManager;
    private String dataSourceName;
    private int batchSize;
    private int fetchSize;
    private int maxRows;
    private int queryTimeout;

    public Builder setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
      return this;
    }

    public Builder setDialect(Dialect dialect) {
      this.dialect = dialect;
      return this;
    }

    public Builder setSqlFileRepository(SqlFileRepository sqlFileRepository) {
      this.sqlFileRepository = sqlFileRepository;
      return this;
    }

    public Builder setScriptFileLoader(ScriptFileLoader scriptFileLoader) {
      this.scriptFileLoader = scriptFileLoader;
      return this;
    }

    public Builder setJdbcLogger(JdbcLogger jdbcLogger) {
      this.jdbcLogger = jdbcLogger;
      return this;
    }

    public Builder setRequiresNewController(RequiresNewController requiresNewController) {
      this.requiresNewController = requiresNewController;
      return this;
    }

    public Builder setClassHelper(ClassHelper classHelper) {
      this.classHelper = classHelper;
      return this;
    }

    public Builder setCommandImplementors(CommandImplementors commandImplementors) {
      this.commandImplementors = commandImplementors;
      return this;
    }

    public Builder setQueryImplementors(QueryImplementors queryImplementors) {
      this.queryImplementors = queryImplementors;
      return this;
    }

    public Builder setExceptionSqlLogType(SqlLogType exceptionSqlLogType) {
      this.exceptionSqlLogType = exceptionSqlLogType;
      return this;
    }

    public Builder setUnknownColumnHandler(UnknownColumnHandler unknownColumnHandler) {
      this.unknownColumnHandler = unknownColumnHandler;
      return this;
    }

    public Builder setNaming(Naming naming) {
      this.naming = naming;
      return this;
    }

    public Builder setMapKeyNaming(MapKeyNaming mapKeyNaming) {
      this.mapKeyNaming = mapKeyNaming;
      return this;
    }

    public Builder setCommenter(Commenter commenter) {
      this.commenter = commenter;
      return this;
    }

    public Builder setEntityListenerProvider(EntityListenerProvider entityListenerProvider) {
      this.entityListenerProvider = entityListenerProvider;
      return this;
    }

    public Builder setTransactionManager(TransactionManager transactionManager) {
      this.transactionManager = transactionManager;
      return this;
    }

    public Builder setDataSourceName(String dataSourceName) {
      this.dataSourceName = dataSourceName;
      return this;
    }

    public Builder setBatchSize(int batchSize) {
      this.batchSize = batchSize;
      return this;
    }

    public Builder setFetchSize(int fetchSize) {
      this.fetchSize = fetchSize;
      return this;
    }

    public Builder setMaxRows(int maxRows) {
      this.maxRows = maxRows;
      return this;
    }

    public Builder setQueryTimeout(int queryTimeout) {
      this.queryTimeout = queryTimeout;
      return this;
    }

    public DbConfig build() {
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
  }
}
