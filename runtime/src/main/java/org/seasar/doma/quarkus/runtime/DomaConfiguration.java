package org.seasar.doma.quarkus.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.NoCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.dialect.MssqlDialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

@ConfigRoot
public class DomaConfiguration {

  /**
   * The SQL dialect.
   *
   * @see Config#getDialect()
   * @asciidoclet
   */
  @ConfigItem(defaultValueDocumentation = "depends on 'quarkus.datasource.db-kind'")
  public Optional<DialectType> dialect;

  /**
   * The SQL file repository.
   *
   * @see Config#getSqlFileRepository()
   */
  @ConfigItem(defaultValue = "greedy-cache")
  public SqlFileRepositoryType sqlFileRepository;

  /**
   * The naming convention controller.
   *
   * @see Config#getNaming()
   */
  @ConfigItem(defaultValue = "none")
  public NamingType naming;

  /**
   * The SQL log type that determines the SQL log format in exceptions.
   *
   * @see Config#getExceptionSqlLogType()
   */
  @ConfigItem(defaultValue = "none")
  public SqlLogType exceptionSqlLogType;

  /**
   * The name of the data source.
   *
   * @see Config#getDataSourceName()
   */
  @ConfigItem(
      defaultValueDocumentation = "depends on 'quarkus.datasource.\"datasource-name\".db-kind'")
  public Optional<String> datasourceName;

  /**
   * The batch size.
   *
   * @see Config#getBatchSize()
   */
  @ConfigItem(defaultValue = "0")
  public int batchSize;

  /**
   * The fetch size.
   *
   * @see Config#getFetchSize()
   */
  @ConfigItem(defaultValue = "0")
  public int fetchSize;

  /**
   * The max rows.
   *
   * @see Config#getMaxRows()
   */
  @ConfigItem(defaultValue = "0")
  public int maxRows;

  /**
   * The query timeout limit in seconds.
   *
   * @see Config#getQueryTimeout()
   */
  @ConfigItem(defaultValue = "0")
  public int queryTimeout;

  /** The log configuration. */
  @ConfigItem public LogConfiguration log;

  @Override
  public String toString() {
    return "DomaConfiguration{"
        + "dialect="
        + dialect
        + ", sqlFileRepository="
        + sqlFileRepository
        + ", naming="
        + naming
        + ", exceptionSqlLogType="
        + exceptionSqlLogType
        + ", datasourceName="
        + datasourceName
        + ", batchSize="
        + batchSize
        + ", fetchSize="
        + fetchSize
        + ", maxRows="
        + maxRows
        + ", queryTimeout="
        + queryTimeout
        + ", log="
        + log
        + '}';
  }

  public enum DialectType {
    STANDARD(StandardDialect::new),
    MSSQL(MssqlDialect::new),
    MYSQL(MysqlDialect::new),
    POSTGRES(PostgresDialect::new),
    H2(H2Dialect::new);

    private final Supplier<Dialect> constructor;

    DialectType(Supplier<Dialect> constructor) {
      this.constructor = constructor;
    }

    public Dialect create() {
      return this.constructor.get();
    }
  }

  public enum SqlFileRepositoryType {
    NO_CACHE(NoCacheSqlFileRepository::new),
    GREEDY_CACHE(GreedyCacheSqlFileRepository::new);

    private final Supplier<SqlFileRepository> constructor;

    SqlFileRepositoryType(Supplier<SqlFileRepository> constructor) {
      this.constructor = constructor;
    }

    public SqlFileRepository create() {
      return this.constructor.get();
    }
  }

  public enum NamingType {
    NONE(Naming.NONE),
    LOWER_CASE(Naming.LOWER_CASE),
    UPPER_CASE(Naming.UPPER_CASE),
    SNAKE_LOWER_CASE(Naming.SNAKE_LOWER_CASE),
    SNAKE_UPPER_CASE(Naming.SNAKE_UPPER_CASE);

    private final Naming naming;

    NamingType(Naming naming) {
      this.naming = naming;
    }

    public Naming naming() {
      return this.naming;
    }
  }
}
