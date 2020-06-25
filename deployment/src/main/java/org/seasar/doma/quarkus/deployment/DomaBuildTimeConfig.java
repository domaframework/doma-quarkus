package org.seasar.doma.quarkus.deployment;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;
import java.util.Optional;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.quarkus.runtime.DomaSettings;

@ConfigRoot
public class DomaBuildTimeConfig {

  public static final String SQL_LOAD_SCRIPT_DEFAULT = "import.sql";
  public static final String SQL_LOAD_SCRIPT_NO_FILE = "no-file";

  /**
   * The SQL dialect.
   *
   * @see Config#getDialect()
   */
  @ConfigItem(defaultValueDocumentation = "depends on 'quarkus.datasource.db-kind'")
  public Optional<DomaSettings.DialectType> dialect;

  /**
   * The SQL file repository.
   *
   * @see Config#getSqlFileRepository()
   */
  @ConfigItem(defaultValue = "greedy-cache")
  public DomaSettings.SqlFileRepositoryType sqlFileRepository;

  /**
   * The naming convention controller.
   *
   * @see Config#getNaming()
   */
  @ConfigItem(defaultValue = "none")
  public DomaSettings.NamingType naming;

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

  /**
   * Name of the file containing the SQL statements to execute when Doma starts. Its default value
   * differs depending on the Quarkus launch mode:
   *
   * <p>* In dev and test modes, it defaults to `import.sql`. Simply add an `import.sql` file in the
   * root of your resources directory and it will be picked up without having to set this property.
   * Pass `no-file` to force Doma to ignore the SQL import file. * In production mode, it defaults
   * to `no-file`. It means Doma won't try to execute any SQL import file by default. Pass an
   * explicit value to force Doma to execute the SQL import file.
   */
  @ConfigItem(defaultValueDocumentation = "import.sql in DEV, TEST ; no-file otherwise")
  public Optional<String> sqlLoadScript;

  /** The log configuration. */
  @ConfigItem public LogBuildTimeConfig log;

  @ConfigGroup
  public static class LogBuildTimeConfig {

    /** Shows SQL logs. */
    @ConfigItem public boolean sql;

    /** Shows DAO logs. */
    @ConfigItem public boolean dao;

    /** Shows the logs of the failure to close JDBC resource. */
    @ConfigItem public boolean closingFailure;

    @Override
    public String toString() {
      return "DomaConfigurationLog{"
          + "sql="
          + sql
          + ", dao="
          + dao
          + ", closingFailure="
          + closingFailure
          + '}';
    }
  }

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
}
