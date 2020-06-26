package org.seasar.doma.quarkus.runtime;

import java.util.function.Supplier;
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

public class DomaSettings {

  public DialectType dialect;
  public SqlFileRepositoryType sqlFileRepository;
  public NamingType naming;
  public SqlLogType exceptionSqlLogType;
  public String dataSourceName;
  public int batchSize;
  public int fetchSize;
  public int maxRows;
  public int queryTimeout;
  public String sqlLoadScript;
  public LogSettings log;

  public static class LogSettings {
    public boolean sql;
    public boolean dao;
    public boolean closingFailure;

    public LogSettings() {}

    public LogSettings(boolean sql, boolean dao, boolean closingFailure) {
      this.sql = sql;
      this.dao = dao;
      this.closingFailure = closingFailure;
    }

    public LogPreferences asLogPreferences() {
      return new LogPreferences(sql, dao, closingFailure);
    }
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

  @Override
  public String toString() {
    return "DomaSettings{"
        + "dialect="
        + dialect
        + ", sqlFileRepository="
        + sqlFileRepository
        + ", naming="
        + naming
        + ", exceptionSqlLogType="
        + exceptionSqlLogType
        + ", dataSourceName='"
        + dataSourceName
        + '\''
        + ", batchSize="
        + batchSize
        + ", fetchSize="
        + fetchSize
        + ", maxRows="
        + maxRows
        + ", queryTimeout="
        + queryTimeout
        + ", sqlLoadScript='"
        + sqlLoadScript
        + '\''
        + ", log="
        + log
        + '}';
  }
}
