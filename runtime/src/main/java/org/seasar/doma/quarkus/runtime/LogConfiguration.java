package org.seasar.doma.quarkus.runtime;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import org.jboss.logging.Logger;

@ConfigGroup
public class LogConfiguration {

  /** The level of log. */
  @ConfigItem(defaultValue = "DEBUG")
  public Logger.Level level = Logger.Level.DEBUG;

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
