package org.seasar.doma.quarkus.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.NoCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.dialect.Db2Dialect;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.dialect.HsqldbDialect;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;
import org.seasar.doma.jdbc.dialect.MssqlDialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.SqliteDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

@ConfigRoot
public class DomaConfiguration {

  /** */
  @ConfigItem public Optional<DialectType> dialect;

  /** */
  @ConfigItem(defaultValue = "greedy-cache")
  public SqlFileRepositoryType sqlFileRepository;

  /** */
  @ConfigItem(defaultValue = "default")
  public NamingType naming;

  /** */
  @ConfigItem(defaultValue = "none")
  public SqlLogType exceptionSqlLogType;

  /** */
  @ConfigItem public Optional<String> datasourceName;

  /** */
  @ConfigItem(defaultValue = "0")
  public int batchSize;

  /** */
  @ConfigItem(defaultValue = "0")
  public int fetchSize;

  /** */
  @ConfigItem(defaultValue = "0")
  public int maxRows;

  /** */
  @ConfigItem(defaultValue = "0")
  public int queryTimeout;

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
        + ", dataSourceName="
        + datasourceName
        + ", batchSize="
        + batchSize
        + ", fetchSize="
        + fetchSize
        + ", maxRows="
        + maxRows
        + ", queryTimeout="
        + queryTimeout
        + '}';
  }

  public enum DialectType {
    STANDARD(StandardDialect::new),
    SQLITE(SqliteDialect::new),
    DB2(Db2Dialect::new),
    MSSQL(MssqlDialect::new),
    MSSQL2008(Mssql2008Dialect::new),
    MYSQL(MysqlDialect::new),
    POSTGRES(PostgresDialect::new),
    ORACLE(OracleDialect::new),
    H2(H2Dialect::new),
    HSQL(HsqldbDialect::new);

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
    SNAKE_UPPER_CASE(Naming.SNAKE_UPPER_CASE),
    LENIENT_SNAKE_LOWER_CASE(Naming.LENIENT_SNAKE_LOWER_CASE),
    LENIENT_SNAKE_UPPER_CASE(Naming.LENIENT_SNAKE_UPPER_CASE),
    DEFAULT(Naming.DEFAULT);

    private final Naming naming;

    NamingType(Naming naming) {
      this.naming = naming;
    }

    public Naming naming() {
      return this.naming;
    }
  }
}
